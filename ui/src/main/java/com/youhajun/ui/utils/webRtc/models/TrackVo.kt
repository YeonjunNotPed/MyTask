package com.youhajun.ui.utils.webRtc.models

import org.webrtc.AudioTrack
import org.webrtc.VideoTrack


data class TrackVo(
    val trackType: TrackType,
    val videoTrack: VideoTrack? = null,
    val audioTrack: AudioTrack? = null
)