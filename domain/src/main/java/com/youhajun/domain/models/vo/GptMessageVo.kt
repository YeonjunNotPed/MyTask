package com.youhajun.domain.models.vo

import com.youhajun.domain.models.enums.GptMessageType
import com.youhajun.domain.models.enums.GptType
import com.youhajun.domain.models.sealeds.GptMessageFooterState

data class GptMessageVo(
    val idx:Int,
    val channelIdx:Int,
    val gptMessageType: GptMessageType,
    val message:String,
    val createAt:String,
    val gptType: GptType,
    val messageFooterState: List<GptMessageFooterState>,
)