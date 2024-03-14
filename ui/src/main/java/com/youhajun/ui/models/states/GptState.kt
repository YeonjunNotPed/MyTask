package com.youhajun.ui.models.states

import com.youhajun.domain.models.enums.GptType
import com.youhajun.domain.models.vo.GptChannelVo
import com.youhajun.domain.models.vo.GptMessageVo

data class GptState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val gptType: GptType = GptType.CHAT_GPT_3_5_TURBO,
    val gptChannelList: List<GptChannelVo> = emptyList(),
    val gptMessageList: List<GptMessageVo> = emptyList()
)