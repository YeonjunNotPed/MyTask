package com.youhajun.data.models.dto.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptMessageResponse(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String,
)