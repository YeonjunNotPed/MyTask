package com.youhajun.data.models.dto.gpt

import com.google.gson.annotations.SerializedName

data class ChatGptResponse(
    @SerializedName("choices")
    val choices: List<ChatGptChoiceDto>,

    @SerializedName("created")
    val created: Int,

    @SerializedName("id")
    val id: String,

    @SerializedName("model")
    val model: String,

    @SerializedName("object")
    val `object`: String,
)