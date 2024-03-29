package com.youhajun.ui.models.states

import com.youhajun.domain.models.enums.WebRTCSessionType
import com.youhajun.ui.utils.webRtc.models.TrackType
import com.youhajun.ui.utils.webRtc.models.VideoTrackVo
import kotlinx.coroutines.flow.StateFlow
import org.webrtc.EglBase

data class LiveRoomState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val webRTCSessionType: WebRTCSessionType = WebRTCSessionType.Offline,
    val videoTrackMap: Map<String, List<VideoTrackVo>> = emptyMap(),
    val mySessionId: String,
    val eglContext: EglBase.Context,
) {
    val myVideoTrack = videoTrackMap[mySessionId]?.find { it.trackType == TrackType.VIDEO }
}