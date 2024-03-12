package com.youhajun.ui.utils.webRtc.models

import org.webrtc.VideoTrack


data class VideoTrackListVo(
    val trackList: List<VideoTrackVo>,
    val sessionId: String
)

data class VideoTrackVo(
    val trackType: TrackType,
    val videoTrack: VideoTrack
)