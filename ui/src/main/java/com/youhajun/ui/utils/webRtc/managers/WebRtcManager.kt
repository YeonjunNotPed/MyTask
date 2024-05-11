package com.youhajun.ui.utils.webRtc.managers

import com.youhajun.common.DefaultDispatcher
import com.youhajun.model_ui.types.webrtc.SignalingCommandType
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.WebRTCContract.Companion.ICE_SEPARATOR
import com.youhajun.ui.utils.webRtc.WebRTCContract.Companion.ID_SEPARATOR
import com.youhajun.model_ui.vo.webrtc.SessionInfoVo
import com.youhajun.model_ui.types.webrtc.StreamPeerType
import com.youhajun.model_ui.types.webrtc.TrackType
import com.youhajun.model_ui.vo.webrtc.TrackVo
import com.youhajun.ui.utils.webRtc.peer.StreamPeerConnection
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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

    override val mySessionId: String = peerConnectionFactory.sessionId
    private val sessionManagerScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    private var localAddedJob: Job? = null
    private var iceCollectJob:Job? = null
    private val pendingIceSharedFlow = MutableSharedFlow<String>(replay = 10, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.SUSPEND)

    override val audioLevelListFlow: Flow<List<Float>> = audioManager.audioLevelPerTimeSharedFlow.onEach {
        updateAudioLevelList(mySessionId, it)
    }

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
        onCollectSignalingCommand()
    }

    override fun flipCamera() {
        videoManager.flipCamera {
            it.onSuccess { isFrontCamera ->
                editSessionInfo(mySessionId) {
                    it.copy(
                        callMediaStateHolder = it.callMediaStateHolder.copy(isFrontCamera = isFrontCamera)
                    )
                }
            }
        }
    }

    override fun setEnableCamera(enabled: Boolean) {
        videoManager.setEnableCamera(enabled)
        editSessionInfo(mySessionId) {
            it.copy(
                callMediaStateHolder = it.callMediaStateHolder.copy(isCameraEnable = enabled)
            )
        }
    }

    override fun setEnableSpeakerphone(enabled: Boolean) {
        audioManager.setEnableSpeakerphone(enabled)
        editSessionInfo(mySessionId) {
            it.copy(
                callMediaStateHolder = it.callMediaStateHolder.copy(isSpeakerEnable = enabled)
            )
        }
    }

    override fun setMicMute(isMute: Boolean) {
        audioManager.setMicMute(isMute)
        editSessionInfo(mySessionId) {
            it.copy(
                callMediaStateHolder = it.callMediaStateHolder.copy(isMicMute = isMute)
            )
        }
    }

    /**
     * 로컬 화면 준비 완료되면 localTrack 을 add
     */
    override fun onScreenReady() {
        localAddedJob = sessionManagerScope.launch {
            audioManager.addLocalAudioTrack {
                peerConnection.addTrack(it, it.id())
                handleOnAddedTrack(mySessionId, it, trackType = TrackType.AUDIO)
            }
            videoManager.addLocalVideoTrack {
                peerConnection.addTrack(it, it.id())
                handleOnAddedTrack(mySessionId, it, trackType = TrackType.VIDEO)
            }
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

        localAddedJob?.cancel()
        localAddedJob = null

        iceCollectJob?.cancel()
        iceCollectJob = null
    }

    /**
     * offer sdp 생성 및 localDescription 에 저장 후 Offer 전송
     */
    private suspend fun sendOffer() {
        localAddedJob?.join()
        val offer = peerConnection.createOffer().getOrThrow()
        val result = peerConnection.setLocalDescription(offer)
        result.onSuccess {
            collectPendingIce()
            signaling.sendCommand(SignalingCommandType.OFFER, offer.description)
        }
    }

    /**
     * answer sdp 생성 및 localDescription 에 저장 후 answer 전송
     */
    private suspend fun sendAnswer() {
        localAddedJob?.join()
        val answer = peerConnection.createAnswer().getOrThrow()
        val result = peerConnection.setLocalDescription(answer)
        result.onSuccess {
            collectPendingIce()
            signaling.sendCommand(SignalingCommandType.ANSWER, answer.description)
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
        val trackVo: TrackVo = when(track.kind()) {
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
            val mergedList = it.trackList + trackVo
            it.copy(trackList = mergedList.toImmutableList())
        }
    }

    private fun removeTrack(sessionId: String, trackType: TrackType) {
        editSessionInfo(sessionId) {
            it.copy(trackList = it.trackList.filterNot { it.trackType == trackType }.toImmutableList())
        }
    }

    private fun editSessionInfo(sessionId: String, editBlock: (SessionInfoVo) -> SessionInfoVo) {
        val newMap = sessionFlow.value.toMutableMap().apply {
            val sessionInfo = this.getOrDefault(sessionId, SessionInfoVo(sessionId = sessionId))
            this[sessionId] = editBlock(sessionInfo)
        }
        sessionManagerScope.launch {
            _sessionFlow.update { newMap }
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
                signaling.sendCommand(SignalingCommandType.ICE, it)
            }
        }
    }

    private fun onCollectSignalingCommand() {
        sessionManagerScope.launch {
            signaling.signalingCommandFlow.collect { commandToValue ->
                when (commandToValue.first) {
                    SignalingCommandType.OFFER -> handleOffer(commandToValue.second)
                    SignalingCommandType.ANSWER -> handleAnswer(commandToValue.second)
                    SignalingCommandType.ICE -> handleIce(commandToValue.second)
                    else -> Unit
                }
            }
        }
    }

    override fun updateAudioLevelList(sessionId: String, audioLevelList: List<Float>) {
        editSessionInfo(sessionId) {
            it.copy(
                callMediaStateHolder = it.callMediaStateHolder.copy(audioLevelList = audioLevelList.toImmutableList())
            )
        }
    }
}