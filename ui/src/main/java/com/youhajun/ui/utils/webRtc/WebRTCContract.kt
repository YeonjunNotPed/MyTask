package com.youhajun.ui.utils.webRtc

import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.ui.utils.webRtc.managers.WebRtcManager
import com.youhajun.ui.utils.webRtc.models.SessionInfoVo
import com.youhajun.ui.utils.webRtc.models.StreamPeerType
import com.youhajun.ui.utils.webRtc.peer.StreamPeerConnection
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStreamTrack
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

interface WebRTCContract {

    companion object {
        const val ICE_SEPARATOR = '$'
        const val ID_SEPARATOR = ':'
    }

    interface PeerConnectionFactory {

        val sessionId: String

        fun makePeerConnection(
            type: StreamPeerType,
            peerConnectionListener: PeerConnectionListener,
        ): StreamPeerConnection

        fun makeVideoSource(isScreencast: Boolean): VideoSource
        fun makeVideoTrack(source: VideoSource, trackId: String): VideoTrack
        fun makeAudioSource(constraints: MediaConstraints): AudioSource
        fun makeAudioTrack(source: AudioSource, trackId: String): AudioTrack
    }

    interface PeerConnectionListener {
        fun onStreamAdded(id: String, track:MediaStreamTrack)

        fun onIceCandidate(ice: IceCandidate, peerType: StreamPeerType)
    }

    interface Signaling {
        val signalingCommandFlow: Flow<Pair<SignalingType, String>>
        fun sendCommand(signalingCommand: SignalingType, message: String)
    }

    @AssistedFactory
    interface Factory {
        fun createSessionManager(signaling: Signaling): WebRtcManager
    }

    interface SessionManager {
        val mySessionId: String
        val sessionFlow: StateFlow<Map<String, SessionInfoVo>>
        val audioLevelListFlow: Flow<List<Float>>
        fun flipCamera()
        fun setEnableCamera(enabled: Boolean)
        fun setMicMute(isMute: Boolean)
        fun setEnableSpeakerphone(enabled: Boolean)
        fun onScreenReady()
        fun onSignalingImpossible()
        fun disconnect()
        fun updateAudioLevelList(sessionId: String, audioLevelList: List<Float>)
    }

    interface AudioManager {
        val audioLevelPerTimeSharedFlow: SharedFlow<List<Float>>
        fun setMicMute(isMute: Boolean)
        fun setEnableSpeakerphone(enabled: Boolean)
        fun addLocalAudioTrack(addTrack:(MediaStreamTrack)-> Unit)
        fun dispose()
    }

    interface VideoManager {

        fun flipCamera(result: (Result<Boolean>) -> Unit)
        fun setEnableCamera(enabled: Boolean)
        fun addLocalVideoTrack(addTrack:(MediaStreamTrack)-> Unit)
        fun dispose()
    }
}