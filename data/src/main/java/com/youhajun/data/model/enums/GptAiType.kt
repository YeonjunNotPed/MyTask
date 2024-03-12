package com.youhajun.data.model.enums

import com.youhajun.data.Endpoint

enum class GptAiType(val type: String) {
    CHAT_GPT("chat_gpt"), GEMINI("gemini");

    companion object {
        fun fromUrl(url: String): GptAiType = when {
            url.startsWith(Endpoint.ChatGpt.BASE_URL) -> CHAT_GPT
            url.startsWith(Endpoint.Gemini.BASE_URL) -> GEMINI
            else -> CHAT_GPT
        }
    }
}
