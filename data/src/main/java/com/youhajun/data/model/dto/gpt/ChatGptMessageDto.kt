package com.youhajun.data.model.dto.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptMessageDto(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String,
)