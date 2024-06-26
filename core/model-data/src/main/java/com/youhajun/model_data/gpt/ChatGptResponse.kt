package com.youhajun.model_data.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("choices")
    val messageList: List<ChatGptMessageInfoResponse>,

    @SerializedName("created")
    val created: Long,

    @SerializedName("model")
    val model: String,
)