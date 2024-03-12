package com.youhajun.domain.model.vo

import com.youhajun.domain.model.enums.GptMessageType
import com.youhajun.domain.model.enums.GptType
import com.youhajun.domain.model.sealeds.GptMessageFooterState

data class GptMessageVo(
    val idx:Int,
    val channelIdx:Int,
    val gptMessageType: GptMessageType,
    val message:String,
    val createAt:String,
    val gptType: GptType,
    val messageFooterState: List<GptMessageFooterState>,
)