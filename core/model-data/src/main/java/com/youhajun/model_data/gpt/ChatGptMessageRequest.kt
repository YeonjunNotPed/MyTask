package com.youhajun.model_data.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptMessageRequest(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String,
)