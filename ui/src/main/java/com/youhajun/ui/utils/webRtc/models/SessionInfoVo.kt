package com.youhajun.ui.utils.webRtc.models

import com.youhajun.ui.models.holder.CallMediaStateHolder

data class SessionInfoVo(
    val callMediaStateHolder: CallMediaStateHolder = CallMediaStateHolder(),
    val trackList: List<TrackVo> = emptyList()
) {
    fun findTrack(trackType: TrackType): TrackVo? {
        return this.trackList.find { it.trackType == trackType }
    }
}