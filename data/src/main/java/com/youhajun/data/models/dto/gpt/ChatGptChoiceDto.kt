package com.youhajun.data.models.dto.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptChoiceDto(
    @SerializedName("index")
    val index: Int,
    @SerializedName("message")
    val messageDto: ChatGptMessageDto,
)