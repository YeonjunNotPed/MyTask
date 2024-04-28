package com.youhajun.model_ui.states

import com.youhajun.model_ui.types.webrtc.WebRTCSessionType
import com.youhajun.model_ui.vo.webrtc.SessionInfoVo
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper

data class LiveRoomState(
    val eglContextWrapper: EglBaseContextWrapper,
    val mySessionId: String,
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val webRTCSessionType: WebRTCSessionType = WebRTCSessionType.Offline,
    val mySessionInfoVo: SessionInfoVo? = null,
    val partnerSessionInfoVo: SessionInfoVo? = null,
    val isVisibleBottomAction: Boolean = true,
)