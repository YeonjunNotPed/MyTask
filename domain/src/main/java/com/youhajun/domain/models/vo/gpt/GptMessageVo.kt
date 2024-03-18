package com.youhajun.domain.models.vo.gpt

import com.youhajun.domain.models.enums.GptMessageType
import com.youhajun.domain.models.sealeds.GptMessageFooterState

data class GptMessageVo(
    val idx:Long,
    val assistantIdx:Long,
    val gptMessageType: GptMessageType,
    val message:String,
    val createdAtUnixTimeStamp: Long,
    val messageFooterState: List<GptMessageFooterState> = emptyList(),
)