package com.youhajun.model_ui.vo.webrtc

import com.youhajun.model_ui.holder.CallMediaStateHolder
import com.youhajun.model_ui.types.webrtc.TrackType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class SessionInfoVo(
    val sessionId: String,
    val callMediaStateHolder: CallMediaStateHolder = CallMediaStateHolder(),
    val trackList: ImmutableList<TrackVo> = persistentListOf()
)

fun SessionInfoVo.toSessionTrackInfoVo(trackType: TrackType): SessionTrackInfoVo? =
    trackList.find { track -> track.trackType == trackType }?.let { track ->
        SessionTrackInfoVo(
            sessionId = sessionId,
            callMediaStateHolder = callMediaStateHolder,
            trackVo = track
        )
    }

fun Collection<SessionInfoVo>.toSessionVideoTrackVoList(): ImmutableList<SessionTrackInfoVo> =
    flatMap { sessionInfo ->
        sessionInfo.trackList
            .filter { it.trackType == TrackType.VIDEO || it.trackType == TrackType.SCREEN_SHARE_VIDEO }
            .mapNotNull { track ->
                sessionInfo.toSessionTrackInfoVo(track.trackType)
            }
    }.toImmutableList()