package com.youhajun.model_ui.states

import com.youhajun.model_ui.types.webrtc.TrackType
import com.youhajun.model_ui.types.webrtc.VideoScreenType
import com.youhajun.model_ui.types.webrtc.WebRTCSessionType
import com.youhajun.model_ui.vo.webrtc.SessionTrackInfoVo
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class LiveRoomState(
    val eglContextWrapper: EglBaseContextWrapper,
    val mySessionId: String,
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val webRTCSessionType: WebRTCSessionType = WebRTCSessionType.Offline,
    val sessionTrackInfoList: ImmutableList<SessionTrackInfoVo> = persistentListOf(),
    val fillMaxSessionTrackInfo: SessionTrackInfoVo? = null,
    val floatingSessionTrackInfo: SessionTrackInfoVo? = null,
    val isVisibleBottomAction: Boolean = true,
    val videoScreenType: VideoScreenType = VideoScreenType.FLOATING
) {
    val myVideoSessionTrackInfoVo: SessionTrackInfoVo? = this.sessionTrackInfoList.find { it.sessionId == mySessionId && it.trackVo.trackType == TrackType.VIDEO }
}