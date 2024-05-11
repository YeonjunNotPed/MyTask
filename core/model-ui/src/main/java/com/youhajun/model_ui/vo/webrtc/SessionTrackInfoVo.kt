package com.youhajun.model_ui.vo.webrtc

import com.youhajun.model_ui.holder.CallMediaStateHolder
import com.youhajun.model_ui.types.webrtc.TrackType

data class SessionTrackInfoVo(
    val sessionId: String,
    val callMediaStateHolder: CallMediaStateHolder,
    val trackVo: TrackVo
)

fun List<SessionTrackInfoVo>.partnerSessionTrackInfo(
    sessionId: String,
    trackType: TrackType
): SessionTrackInfoVo? = firstOrNull { it.sessionId != sessionId && it.trackVo.trackType == trackType }

fun List<SessionTrackInfoVo>.findSessionTrackInfo(
    sessionId: String,
    trackType: TrackType
): SessionTrackInfoVo? = find { it.sessionId == sessionId && it.trackVo.trackType == trackType }

fun List<SessionTrackInfoVo>.findSessionTrackInfoById(
    trackId: String?,
): SessionTrackInfoVo? = find { it.trackVo.videoTrack?.id() == trackId }