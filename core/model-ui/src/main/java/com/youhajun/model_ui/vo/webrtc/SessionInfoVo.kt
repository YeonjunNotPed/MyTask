package com.youhajun.model_ui.vo.webrtc

import com.youhajun.model_ui.types.webrtc.TrackType
import com.youhajun.model_ui.holder.CallMediaStateHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SessionInfoVo(
    val callMediaStateHolder: CallMediaStateHolder = CallMediaStateHolder(),
    val trackList: ImmutableList<TrackVo> = persistentListOf()
) {
    fun findTrack(trackType: TrackType): TrackVo? {
        return this.trackList.find { it.trackType == trackType }
    }
}