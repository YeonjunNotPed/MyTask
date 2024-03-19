package com.youhajun.data.models.dto.gpt

data class UpdateGptChannelInfoRequest(
    val idx:Long,
    val gptType: String,
    val roleOfAi: String?,
    val lastQuestion: String,
)