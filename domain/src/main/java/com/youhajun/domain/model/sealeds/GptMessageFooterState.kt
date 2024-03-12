package com.youhajun.domain.model.sealeds

import com.youhajun.domain.model.vo.GptRecommendUrlVo

sealed class GptMessageFooterState {
    object ShowCreateAt: GptMessageFooterState()
    data class ShowRecommendingUrl(val recommendUrlVoList: List<GptRecommendUrlVo>) : GptMessageFooterState()
}