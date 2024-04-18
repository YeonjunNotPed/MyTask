package com.youhajun.model_data.gpt

data class GptAssistantEntityDto(
    val idx: Long = 0,
    val channelIdx: Long,
    val assistantMessage: String,
    val createdAtUnixTimeStamp: Long,
)