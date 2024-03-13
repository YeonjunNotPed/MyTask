package com.youhajun.ui.models.states

import com.youhajun.domain.models.enums.WebRTCSessionType
import org.webrtc.VideoTrack

data class LiveRoomState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val webRTCSessionType: WebRTCSessionType = WebRTCSessionType.Offline,
    val localVideoTrack: VideoTrack? = null,
    val remoteVideoTrack: VideoTrack? = null
)