package com.youhajun.ui.models.states

import com.youhajun.domain.models.enums.WebRTCSessionType
import com.youhajun.ui.utils.webRtc.models.SessionInfoVo
import org.webrtc.EglBase

data class LiveRoomState(
    val eglContext: EglBase.Context,
    val mySessionId: String,
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val webRTCSessionType: WebRTCSessionType = WebRTCSessionType.Offline,
    val mySessionInfoVo: SessionInfoVo? = null,
    val partnerSessionInfoVo: SessionInfoVo? = null,
    val isVisibleBottomAction: Boolean = true,
)