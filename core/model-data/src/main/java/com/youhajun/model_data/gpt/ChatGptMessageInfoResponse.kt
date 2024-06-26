package com.youhajun.model_data.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptMessageInfoResponse(
    @SerializedName("index")
    val index: Int,
    @SerializedName("message")
    val messageDto: ChatGptMessageResponse,
    @SerializedName("logprobs")
    val logprobs: Int,
    @SerializedName("finish_reason")
    val finishReason: String,
)