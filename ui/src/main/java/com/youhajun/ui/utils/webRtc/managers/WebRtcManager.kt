package com.youhajun.ui.utils.webRtc.managers

import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.ui.di.DefaultDispatcher
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.WebRTCContract.Companion.ICE_SEPARATOR
import com.youhajun.ui.utils.webRtc.WebRTCContract.Companion.ID_SEPARATOR
import com.youhajun.ui.utils.webRtc.models.SessionInfoVo
import com.youhajun.ui.utils.webRtc.models.StreamPeerType
import com.youhajun.ui.utils.webRtc.models.TrackType
import com.youhajun.ui.utils.webRtc.models.TrackVo
import com.youhajun.ui.utils.webRtc.peer.StreamPeerConnection
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.webrtc.AudioTrack
import org.webrtc.IceCandidate
import org.webrtc.MediaStreamTrack
import org.webrtc.SessionDescription
import org.webrtc.VideoTrack

class WebRtcManager @AssistedInject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val peerConnectionFactory: WebRTCContract.PeerConnectionFactory,
    @Assisted private val signaling: WebRTCContract.Signaling,
    private val audioManager: WebRTCContract.AudioManager,
    private val videoManager: WebRTCContract.VideoManager,
) : WebRTCContract.SessionManager {

    private val sessionManagerScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    private var iceCollectJob:Job? = null
    private val pendingIceSharedFlow = MutableSharedFlow<String>(
        replay = 10,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.SUSPEND)

    override val mySessionId: String = peerConnectionFactory.sessionId

    private val _sessionFlow = MutableStateFlow<Map<String, SessionInfoVo>>(hashMapOf())
    override val sessionFlow: StateFlow<Map<String, SessionInfoVo>> = _sessionFlow.asStateFlow()

    private val peerConnection: StreamPeerConnection by lazy {
        peerConnectionFactory.makePeerConnection(
            type = StreamPeerType.SUBSCRIBER,
            peerConnectionListener = object : WebRTCContract.PeerConnectionListener {
                override fun onStreamAdded(id: String, track: MediaStreamTrack) {
                    val (trackType, sessionId) = id.split(ID_SEPARATOR).let {
                        TrackType.typeOf(it.first()) to it.last()
                    }
                    handleOnAddedTrack(sessionId, track, trackType)
                }

                override fun onIceCandidate(ice: IceCandidate, peerType: StreamPeerType) {
                    emitPendingIce(ice)
                }
            }
        )
    }

    init {
        sessionManagerScope.launch {
            signaling.signalingCommandFlow.collect { commandToValue ->
                when (commandToValue.first) {
                    SignalingType.OFFER -> handleOffer(commandToValue.second)
                    SignalingType.ANSWER -> handleAnswer(commandToValue.second)
                    SignalingType.ICE -> handleIce(commandToValue.second)
                    else -> Unit
                }
            }
        }
    }

    override fun flipCamera() {
        videoManager.flipCamera {
            it.onSuccess { isFrontCamera ->
                editSessionInfo(mySessionId) {
                    it.copy(
                        callMediaStateVo = it.callMediaStateVo.copy(isFrontCamera = isFrontCamera)
                    )
                }
            }
        }
    }

    override fun setEnableCamera(enabled: Boolean) {
        videoManager.setEnableCamera(enabled)
        editSessionInfo(mySessionId) {
            it.copy(
                callMediaStateVo = it.callMediaStateVo.copy(isCameraEnable = enabled)
            )
        }
    }

    override fun setEnableSpeakerphone(enabled: Boolean) {
        audioManager.setEnableSpeakerphone(enabled)
        editSessionInfo(mySessionId) {
            it.copy(
                callMediaStateVo = it.callMediaStateVo.copy(isSpeakerEnable = enabled)
            )
        }
    }

    override fun setMicMute(isMute: Boolean) {
        audioManager.setMicMute(isMute)
        editSessionInfo(mySessionId) {
            it.copy(
                callMediaStateVo = it.callMediaStateVo.copy(isMicMute = isMute)
            )
        }
    }

    /**
     * 로컬 화면 준비 완료되면 localTrack 을 add
     */

    override fun onScreenReady() {
        audioManager.addLocalAudioTrack {
            peerConnection.addTrack(it, it.id())
            handleOnAddedTrack(mySessionId, it, trackType = TrackType.AUDIO)
        }
        videoManager.addLocalVideoTrack {
            peerConnection.addTrack(it, it.id())
            handleOnAddedTrack(mySessionId, it, trackType = TrackType.VIDEO)
        }
    }

    /**
     * Signaling 타입이 Impossible(인원 미충족)일 때 sendOffer
     */

    override fun onSignalingImpossible() {
        sessionManagerScope.launch {
            sendOffer()
        }
    }

    override fun disconnect() {
        videoManager.dispose()
        audioManager.dispose()
        peerConnection.dispose()
    }

    /**
     * offer sdp 생성 및 localDescription 에 저장 후 Offer 전송
     */
    private suspend fun sendOffer() {
        val offer = peerConnection.createOffer().getOrThrow()
        val result = peerConnection.setLocalDescription(offer)
        result.onSuccess {
            collectPendingIce()
            signaling.sendCommand(SignalingType.OFFER, offer.description)
        }
    }

    /**
     * answer sdp 생성 및 localDescription 에 저장 후 answer 전송
     */
    private suspend fun sendAnswer() {
        val answer = peerConnection.createAnswer().getOrThrow()
        val result = peerConnection.setLocalDescription(answer)
        result.onSuccess {
            collectPendingIce()
            signaling.sendCommand(SignalingType.ANSWER, answer.description)
        }
    }

    /**
     * 받은 offer sdp, remoteDescription 에 저장 및 sendAnswer
     */
    private suspend fun handleOffer(sdp: String) {
        peerConnection.setRemoteDescription(
            SessionDescription(SessionDescription.Type.OFFER, sdp)
        ).onSuccess {
            sendAnswer()
        }
    }

    /**
     * 받은 answer sdp, remoteDescription 에 저장
     */
    private suspend fun handleAnswer(sdp: String) {
        peerConnection.setRemoteDescription(
            SessionDescription(SessionDescription.Type.ANSWER, sdp)
        )
    }

    private suspend fun handleIce(iceMessage: String) {
        val iceArray = iceMessage.split(ICE_SEPARATOR)
        peerConnection.addIceCandidate(
            IceCandidate(
                iceArray[0],
                iceArray[1].toInt(),
                iceArray[2]
            )
        )
    }
    private fun handleOnAddedTrack(sessionId: String, track: MediaStreamTrack, trackType: TrackType) {
        track.setEnabled(true)
        val trackVo:TrackVo = when(track.kind()) {
            MediaStreamTrack.VIDEO_TRACK_KIND -> {
                track as VideoTrack
                TrackVo(trackType, videoTrack = track)
            }
            MediaStreamTrack.AUDIO_TRACK_KIND -> {
                track as AudioTrack
                TrackVo(trackType, audioTrack = track)
            }
            else -> throw RuntimeException("Unknown Track Kind")
        }
        addTrack(sessionId, trackVo)
    }
    private fun addTrack(sessionId: String, trackVo: TrackVo) {
        editSessionInfo(sessionId) {
            it.copy(trackList = it.trackList + trackVo)
        }
    }

    private fun removeTrack(sessionId: String, trackType: TrackType) {
        editSessionInfo(sessionId) {
            it.copy(trackList = it.trackList.filterNot { it.trackType == trackType })
        }
    }

    private fun editSessionInfo(sessionId: String, editBlock: (SessionInfoVo) -> SessionInfoVo) {
        val newMap = sessionFlow.value.toMutableMap().apply {
            val sessionInfo = this.getOrDefault(sessionId, SessionInfoVo())
            this[sessionId] = editBlock(sessionInfo)
        }
        sessionManagerScope.launch {
            _sessionFlow.emit(newMap)
        }
    }

    private fun emitPendingIce(ice: IceCandidate) {
        sessionManagerScope.launch {
            val iceMessage = "${ice.sdpMid}$ICE_SEPARATOR${ice.sdpMLineIndex}$ICE_SEPARATOR${ice.sdp}"
            pendingIceSharedFlow.emit(iceMessage)
        }
    }

    private fun collectPendingIce() {
        if(iceCollectJob?.isActive == true) return

        iceCollectJob = sessionManagerScope.launch {
            pendingIceSharedFlow.collect {
                signaling.sendCommand(SignalingType.ICE, it)
            }
        }
    }
}