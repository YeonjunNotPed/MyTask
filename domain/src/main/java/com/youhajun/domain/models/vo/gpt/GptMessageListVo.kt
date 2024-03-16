package com.youhajun.domain.models.vo.gpt

import com.youhajun.domain.models.enums.GptType

data class GptChannelVo(
    val channelIdx:Int,
    val gptType: GptType,
    val lastQuestion: String,
)