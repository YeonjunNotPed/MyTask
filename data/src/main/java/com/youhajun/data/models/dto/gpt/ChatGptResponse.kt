package com.youhajun.data.models.dto.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("choices")
    val messageList: List<ChatGptMessageInfoResponse>,

    @SerializedName("created")
    val created: Int,

    @SerializedName("model")
    val model: String,
)