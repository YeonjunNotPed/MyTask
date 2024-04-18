package com.youhajun.model_data.gpt

data class GptMessageEntityDto(
    val idx: Long = 0,
    val channelIdx: Long,
    val gptMessageType: String,
    val message: String,
    val createdAtUnixTimeStamp: Long,
)