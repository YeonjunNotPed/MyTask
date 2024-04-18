package com.youhajun.model_data.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptRequest(
    @SerializedName("messages")
    val messages: List<ChatGptMessageRequest>,

    @SerializedName("model")
    val model: String,
)