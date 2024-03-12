package com.youhajun.ui.models.states

import com.youhajun.domain.model.vo.GptChannelVo
import com.youhajun.domain.model.vo.GptMessageVo

data class GptState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val gptChannelList: List<GptChannelVo> = emptyList(),
    val gptMessageList: List<GptMessageVo> = emptyList()
)