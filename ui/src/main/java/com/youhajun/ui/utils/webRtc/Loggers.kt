package com.youhajun.ui.utils.webRtc

import com.youhajun.model_ui.types.webrtc.StreamPeerType
import io.getstream.log.taggedLogger
import org.webrtc.AudioTrack
import org.webrtc.CandidatePairChangeEvent
import org.webrtc.IceCandidate
import org.webrtc.IceCandidateErrorEvent
import org.webrtc.Loggable
import org.webrtc.Logging
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.RtpReceiver
import org.webrtc.RtpTransceiver
import org.webrtc.SessionDescription
import org.webrtc.audio.JavaAudioDeviceModule

object Loggers {

    private const val WEB_RTC_TAG = "Call:WebRTC"
    private const val AUDIO_TAG = "Call:AudioTrackCallback"
    private const val CONNECTION_TAG = "Call:PeerConnection"
    private const val STREAM_CONNECTION_TAG = "Call:StreamPeerConnection"

    private val webRtcLogger by taggedLogger(WEB_RTC_TAG)
    private val audioLogger by taggedLogger(AUDIO_TAG)
    private val connectionLogger by taggedLogger(CONNECTION_TAG)
    private val streamConnectionLogger by taggedLogger(STREAM_CONNECTION_TAG)
    fun webRTCLog(): Loggable = Loggable { message, severity, tag ->
        when (severity) {
            Logging.Severity.LS_VERBOSE -> {
                webRtcLogger.v { "[onLogMessage] label: $tag, message: $message" }
            }

            Logging.Severity.LS_INFO -> {
                webRtcLogger.i { "[onLogMessage] label: $tag, message: $message" }
            }

            Logging.Severity.LS_WARNING -> {
                webRtcLogger.w { "[onLogMessage] label: $tag, message: $message" }
            }

            Logging.Severity.LS_ERROR -> {
                webRtcLogger.e { "[onLogMessage] label: $tag, message: $message" }
            }

            Logging.Severity.LS_NONE -> {
                webRtcLogger.d { "[onLogMessage] label: $tag, message: $message" }
            }

            else -> {}
        }
    }

    object Audio {
        fun audioRecordErrorLog(): JavaAudioDeviceModule.AudioRecordErrorCallback =
            object : JavaAudioDeviceModule.AudioRecordErrorCallback {
                override fun onWebRtcAudioRecordInitError(p0: String?) {
                    audioLogger.w { "[onWebRtcAudioRecordInitError] $p0" }
                }

                override fun onWebRtcAudioRecordStartError(
                    p0: JavaAudioDeviceModule.AudioRecordStartErrorCode?,
                    p1: String?
                ) {
                    audioLogger.w { "[onWebRtcAudioRecordInitError] $p1" }
                }

                override fun onWebRtcAudioRecordError(p0: String?) {
                    audioLogger.w { "[onWebRtcAudioRecordError] $p0" }
                }
            }

        fun audioTrackErrorLog(): JavaAudioDeviceModule.AudioTrackErrorCallback =
            object : JavaAudioDeviceModule.AudioTrackErrorCallback {
                override fun onWebRtcAudioTrackInitError(p0: String?) {
                    audioLogger.w { "[onWebRtcAudioTrackInitError] $p0" }
                }

                override fun onWebRtcAudioTrackStartError(
                    p0: JavaAudioDeviceModule.AudioTrackStartErrorCode?,
                    p1: String?
                ) {
                    audioLogger.w { "[onWebRtcAudioTrackStartError] $p0" }
                }

                override fun onWebRtcAudioTrackError(p0: String?) {
                    audioLogger.w { "[onWebRtcAudioTrackError] $p0" }
                }
            }

        fun audioRecordStateLog(): JavaAudioDeviceModule.AudioRecordStateCallback =
            object : JavaAudioDeviceModule.AudioRecordStateCallback {
                override fun onWebRtcAudioRecordStart() {
                    audioLogger.d { "[onWebRtcAudioRecordStart] no args" }
                }

                override fun onWebRtcAudioRecordStop() {
                    audioLogger.d { "[onWebRtcAudioRecordStop] no args" }
                }

            }

        fun audioTrackStateLog(): JavaAudioDeviceModule.AudioTrackStateCallback =
            object : JavaAudioDeviceModule.AudioTrackStateCallback {
                override fun onWebRtcAudioTrackStart() {
                    audioLogger.d { "[onWebRtcAudioTrackStart] no args" }
                }

                override fun onWebRtcAudioTrackStop() {
                    audioLogger.d { "[onWebRtcAudioTrackStop] no args" }
                }
            }
    }

    object StreamConnection {
        private lateinit var typeTag: String

        fun setTypeTag(type: StreamPeerType) {
            typeTag = type.stringify()
        }

        fun initialize(peerConnection: PeerConnection) {
            streamConnectionLogger.d { "[initialize] #sfu; #$typeTag; peerConnection: $peerConnection" }
        }

        fun createOffer() {
            streamConnectionLogger.d { "[createOffer] #sfu; #$typeTag; no args" }
        }

        fun createAnswer() {
            streamConnectionLogger.d { "[createAnswer] #sfu; #$typeTag; no args" }
        }

        fun setRemoteDescription(sessionDescription: SessionDescription) {
            streamConnectionLogger.d { "[setRemoteDescription] #sfu; #$typeTag; answerSdp: ${sessionDescription.stringify()}" }
        }

        fun setLocalDescription(sessionDescription: SessionDescription) {
            streamConnectionLogger.d { "[setLocalDescription] #sfu; #$typeTag; offerSdp: ${sessionDescription.stringify()}" }
        }

        fun successAddIceCandidate(result: Result<Unit>) {
            streamConnectionLogger.v { "[addIceCandidate] #sfu; #$typeTag; completed: $result" }
        }

        fun addIceCandidate(iceCandidate: IceCandidate) {
            streamConnectionLogger.d { "[addIceCandidate] #sfu; #$typeTag; rtcIceCandidate: $iceCandidate" }
        }

        fun pendingIceCandidate(iceCandidate: IceCandidate) {
            streamConnectionLogger.w { "[addIceCandidate] #sfu; #$typeTag; postponed (no remoteDescription): $iceCandidate" }
        }

        fun consumePendingIceCandidate(iceCandidate: IceCandidate) {
            streamConnectionLogger.i { "[setRemoteDescription] #sfu; #subscriber; pendingRtcIceCandidate: $iceCandidate" }
        }
    }

    object Connection {

        private lateinit var typeTag: String

        fun setTypeTag(type: StreamPeerType) {
            typeTag = type.stringify()
        }

        fun onIceCandidate(candidate: IceCandidate?) {
            connectionLogger.i { "[onIceCandidate] #sfu; #$typeTag; candidate: $candidate" }
        }

        fun onAddStream(stream: MediaStream?) {
            connectionLogger.i { "[onAddStream] #sfu; #$typeTag; stream: $stream" }
        }

        fun onAddTrack(receiver: RtpReceiver?, mediaStreams: Array<out MediaStream>?) {
            connectionLogger.i { "[onAddTrack] #sfu; #$typeTag; receiver: $receiver, mediaStreams: $mediaStreams" }
        }

        fun onAddTrackMediaStream(mediaStream: MediaStream) {
            connectionLogger.v { "[onAddTrack] #sfu; #$typeTag; mediaStream: $mediaStream" }
        }

        fun onAddTrackAudioTrack(remoteAudioTrack: AudioTrack) {
            connectionLogger.v { "[onAddTrack] #sfu; #$typeTag; remoteAudioTrack: ${remoteAudioTrack.stringify()}" }
        }

        fun onRenegotiationNeeded() {
            connectionLogger.i { "[onRenegotiationNeeded] #sfu; #$typeTag; no args" }
        }

        fun onIceConnectionChange(newState: PeerConnection.IceConnectionState?) {
            connectionLogger.i { "[onIceConnectionChange] #sfu; #$typeTag; newState: $newState" }
        }

        fun onTrack(transceiver: RtpTransceiver?) {
            connectionLogger.i { "[onTrack] #sfu; #$typeTag; transceiver: $transceiver" }
        }

        fun onRemoveTrack(receiver: RtpReceiver?) {
            connectionLogger.i { "[onRemoveTrack] #sfu; #$typeTag; receiver: $receiver" }
        }

        fun onSignalingChange(newState: PeerConnection.SignalingState?) {
            connectionLogger.d { "[onSignalingChange] #sfu; #$typeTag; newState: $newState" }
        }

        fun onIceConnectionReceivingChange(receiving: Boolean) {
            connectionLogger.i { "[onIceConnectionReceivingChange] #sfu; #$typeTag; receiving: $receiving" }
        }

        fun onIceGatheringChange(newState: PeerConnection.IceGatheringState?) {
            connectionLogger.i { "[onIceGatheringChange] #sfu; #$typeTag; newState: $newState" }
        }

        fun onIceCandidatesRemoved(iceCandidates: Array<out IceCandidate>?) {
            connectionLogger.i { "[onIceCandidatesRemoved] #sfu; #$typeTag; iceCandidates: $iceCandidates" }
        }

        fun onIceCandidateError(event: IceCandidateErrorEvent?) {
            connectionLogger.e { "[onIceCandidateError] #sfu; #$typeTag; event: ${event?.stringify()}" }
        }

        fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
            connectionLogger.i { "[onConnectionChange] #sfu; #$typeTag; newState: $newState" }
        }

        fun onSelectedCandidatePairChanged(event: CandidatePairChangeEvent?) {
            connectionLogger.i { "[onSelectedCandidatePairChanged] #sfu; #$typeTag; event: $event" }
        }
    }
}