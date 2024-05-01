package com.youhajun.model_ui.types.gpt

import com.youhajun.model_ui.vo.gpt.GptRecommendUrlVo
import kotlinx.collections.immutable.ImmutableList

sealed class GptMessageFooterState {
    data object ShowCreateAt: GptMessageFooterState()
    data class ShowRecommendingUrl(val recommendUrlVoList: ImmutableList<GptRecommendUrlVo>) : GptMessageFooterState()
}