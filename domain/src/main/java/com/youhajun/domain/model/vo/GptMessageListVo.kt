package com.youhajun.domain.model.vo

import com.youhajun.domain.model.enums.GptType

data class GptChannelVo(
    val channelIdx:Int,
    val gptType: GptType,
    val lastQuestion: String,
)