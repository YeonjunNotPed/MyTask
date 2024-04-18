package com.youhajun.model_data.gpt

data class UpdateGptChannelInfoRequest(
    val idx:Long,
    val gptType: String,
    val roleOfAi: String?,
    val lastQuestion: String,
)