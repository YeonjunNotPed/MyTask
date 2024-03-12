package com.youhajun.ui.utils.webRtc

import com.youhajun.domain.model.enums.SignalingType
import com.youhajun.ui.utils.webRtc.models.StreamPeerType
import com.youhajun.ui.utils.webRtc.models.VideoTrackListVo
import com.youhajun.ui.utils.webRtc.models.VideoTrackVo
import com.youhajun.ui.utils.webRtc.peer.StreamPeerConnection
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.MediaStreamTrack
import org.webrtc.RtpTransceiver
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

interface WebRTCContract {

    interface PeerConnectionFactory {

        val eglBaseContext: EglBase.Context
        fun makePeerConnection(
            type: StreamPeerType,
            mediaConstraints: MediaConstraints,
            peerConnectionListener: PeerConnectionListener
        ): StreamPeerConnection

        fun makeVideoSource(isScreencast: Boolean): VideoSource
        fun makeVideoTrack(source: VideoSource, trackId: String): VideoTrack
        fun makeAudioSource(constraints: MediaConstraints = MediaConstraints()): AudioSource
        fun makeAudioTrack(source: AudioSource, trackId: String): AudioTrack
    }

    interface PeerConnectionListener {
        fun onStreamAdded(stream: MediaStream)
        fun onNegotiationNeeded(
            streamPeerConnection: StreamPeerConnection,
            peerType: StreamPeerType
        )

        fun onIceCandidate(ice: IceCandidate, peerType: StreamPeerType)
        fun onTrack(rtpTransceiver: RtpTransceiver?)
        fun onIceConnectionConnected()
        fun onIceConnectionCanceled()
    }

    interface Signaling {
        val signalingCommandFlow: Flow<Pair<SignalingType, String>>
        fun dispose()
        fun sendCommand(signalingCommand: SignalingType, message: String)
    }

    @AssistedFactory
    interface Factory {
        fun createSessionManager(signaling: Signaling): WebRtcMeshManager
    }

    interface SessionManager {
        val localVideoTrackFlow: SharedFlow<VideoTrack>

        val remoteVideoTrackFlow: SharedFlow<Map<String, VideoTrackListVo>>
        fun flipCamera()
        fun enableCamera(enabled: Boolean)
        fun enableMicrophone(enabled: Boolean)
        fun onSessionScreenReady()
        fun disconnect()
    }

    interface AudioManager {
        fun enableMicrophone(enabled: Boolean)
        fun addLocalTrackToPeerConnection(addTrack:(MediaStreamTrack)-> Unit)
        fun dispose()
    }

    interface VideoManager {

        val localVideoTrackFlow: SharedFlow<VideoTrack>

        val remoteVideoTrackFlow: SharedFlow<Map<String, VideoTrackListVo>>
        fun onVideoTrack(sessionId:String, videoTrackVo: VideoTrackVo)
        fun flipCamera()
        fun enableCamera(enabled: Boolean)
        fun addLocalTrackToPeerConnection(addTrack:(MediaStreamTrack)-> Unit)
        fun dispose()
    }
}