package com.youhajun.ui.utils.webRtc.managers

import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.ui.utils.webRtc.Loggers
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.models.StreamPeerType
import com.youhajun.ui.utils.webRtc.models.TrackType
import com.youhajun.ui.utils.webRtc.models.VideoTrackListVo
import com.youhajun.ui.utils.webRtc.models.VideoTrackVo
import com.youhajun.ui.utils.webRtc.peer.StreamPeerConnection
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.webrtc.AudioTrack
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.RtpTransceiver
import org.webrtc.SessionDescription
import org.webrtc.VideoTrack
import javax.inject.Singleton

@Singleton
class WebRtcManager @AssistedInject constructor(
    private val peerConnectionFactory: WebRTCContract.PeerConnectionFactory,
    @Assisted private val signaling: WebRTCContract.Signaling,
    private val audioManager: WebRTCContract.AudioManager,
    private val videoManager: WebRTCContract.VideoManager,
) : WebRTCContract.SessionManager {

    companion object {
        private const val ICE_SEPARATOR = '$'
    }

    override val localVideoTrackFlow: SharedFlow<VideoTrack> = videoManager.localVideoTrackFlow
    override val remoteVideoTrackFlow: SharedFlow<Map<String, VideoTrackListVo>> = videoManager.remoteVideoTrackFlow

    private val sessionManagerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val mediaConstraints = MediaConstraints().apply {
        mandatory.addAll(
            listOf(
                MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"),
                MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true")
            )
        )
    }

    private var receivedOfferSdp: String? = null

    private val peerConnection: StreamPeerConnection by lazy {
        peerConnectionFactory.makePeerConnection(
            type = StreamPeerType.SUBSCRIBER,
            mediaConstraints = mediaConstraints,
            peerConnectionListener = object : WebRTCContract.PeerConnectionListener {
                override fun onStreamAdded(stream: MediaStream) {
                    val (trackType, sessionId) = stream.id.split(':').let {
                        TrackType.typeOf(it.first()) to it.last()
                    }

                    stream.audioTracks?.forEach {
                        handleOnAddedAudioTrack(it)
                    }

                    stream.videoTracks?.forEach {
                        handleOnAddedVideoTrack(sessionId, it, trackType)
                    }
                }
                override fun onNegotiationNeeded(streamPeerConnection: StreamPeerConnection, peerType: StreamPeerType) {}
                override fun onIceConnectionConnected() {}
                override fun onIceConnectionCanceled() {}

                override fun onIceCandidate(ice: IceCandidate, peerType: StreamPeerType) {
                    signaling.sendCommand(SignalingType.ICE, "${ice.sdpMid}$ICE_SEPARATOR${ice.sdpMLineIndex}$ICE_SEPARATOR${ice.sdp}")
                }

                override fun onTrack(rtpTransceiver: RtpTransceiver?) {}
            }
        )
    }

    init {
        sessionManagerScope.launch {
            signaling.signalingCommandFlow
                .collect { commandToValue ->
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
        videoManager.flipCamera()
    }

    override fun enableCamera(enabled: Boolean) {
        videoManager.enableCamera(enabled)
    }

    override fun enableMicrophone(enabled: Boolean) {
        audioManager.enableMicrophone(enabled)
    }

    /**
     * 준비 완료되면 localTrack 을 add하고
     * 받은 Offer의 sdp가 있으면 Answer로서 sendAnswer()
     * 없으면 Offer로서 sendOffer()
     */
    override fun onSessionScreenReady() {
        audioManager.addLocalTrackToPeerConnection { peerConnection.connection.addTrack(it) }
        videoManager.addLocalTrackToPeerConnection { peerConnection.connection.addTrack(it) }
        sessionManagerScope.launch {
            if (receivedOfferSdp != null) {
                sendAnswer()
            } else {
                sendOffer()
            }
        }
    }

    override fun disconnect() {
        videoManager.dispose()
        audioManager.dispose()
        signaling.dispose()
    }

    /**
     * offer sdp 생성 및 localDescription 에 저장 후 Offer 전송
     */
    private suspend fun sendOffer() {
        val offer = peerConnection.createOffer().getOrThrow()
        val result = peerConnection.setLocalDescription(offer)
        result.onSuccess {
            signaling.sendCommand(SignalingType.OFFER, offer.description)
        }
    }

    /**
     * 받은 offer sdp, remoteDescription 에 저장
     * answer sdp 생성 및 localDescription 에 저장 후 answer 전송
     */
    private suspend fun sendAnswer() {
        peerConnection.setRemoteDescription(
            SessionDescription(SessionDescription.Type.OFFER, receivedOfferSdp)
        )
        val answer = peerConnection.createAnswer().getOrThrow()
        val result = peerConnection.setLocalDescription(answer)
        result.onSuccess {
            signaling.sendCommand(SignalingType.ANSWER, answer.description)
        }
    }

    /**
     * 받은 offer sdp set
     */
    private fun handleOffer(sdp: String) {
        receivedOfferSdp = sdp
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

    private fun handleOnAddedAudioTrack(audioTrack: AudioTrack) {
        Loggers.Connection.onAddTrackAudioTrack(audioTrack)
        audioTrack.setEnabled(true)
    }

    private fun handleOnAddedVideoTrack(sessionId:String, videoTrack:VideoTrack, trackType: TrackType) {
        videoTrack.setEnabled(true)
        val videoTrackVo = VideoTrackVo(trackType, videoTrack)
        videoManager.onVideoTrack(sessionId, videoTrackVo)
    }
}