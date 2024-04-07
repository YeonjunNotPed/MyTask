package com.youhajun.ui.utils.webRtc.models

import com.youhajun.domain.models.vo.CallMediaStateVo

data class SessionInfoVo(
    val callMediaStateVo: CallMediaStateVo = CallMediaStateVo(),
    val trackList: List<TrackVo> = emptyList()
) {
    fun findTrack(trackType: TrackType): TrackVo? {
        return this.trackList.find { it.trackType == trackType }
    }
}