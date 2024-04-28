package com.youhajun.model_ui.vo.webrtc

import androidx.compose.runtime.Immutable
import com.youhajun.model_ui.types.webrtc.TrackType
import org.webrtc.AudioTrack
import org.webrtc.VideoTrack

@Immutable
data class TrackVo(
    val trackType: TrackType,
    val videoTrack: VideoTrack? = null,
    val audioTrack: AudioTrack? = null
)