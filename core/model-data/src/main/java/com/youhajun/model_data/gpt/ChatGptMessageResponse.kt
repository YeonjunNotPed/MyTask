package com.youhajun.model_data.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptMessageResponse(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String,
)