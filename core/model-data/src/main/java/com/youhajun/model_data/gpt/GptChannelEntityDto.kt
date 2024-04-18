package com.youhajun.model_data.gpt

data class GptChannelEntityDto(
    val idx:Long = 0,
    val gptType: String,
    val roleOfAi: String? = null,
    val lastQuestion: String? = null,
    val createdAtUnixTimeStamp: Long,
)