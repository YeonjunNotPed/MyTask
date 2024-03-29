package com.youhajun.ui.utils.webRtc.models

import org.webrtc.AudioTrack
import org.webrtc.VideoTrack

data class VideoTrackVo(
    val trackType: TrackType,
    val videoTrack: VideoTrack,
    val isCameraEnabled: Boolean = true
)

data class AudioTrackVo(
    val trackType: TrackType,
    val audioTrack: AudioTrack,
    val isMicrophoneEnabled: Boolean = true
)