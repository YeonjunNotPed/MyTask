package com.youhajun.domain.model.enums

enum class GptType(val type: String) {
    CHAT_GPT("chat_gpt"), GEMINI("gemini");

    companion object {
        fun typeOf(value: String): GptType {
            return values().find {
                it.type == value
            } ?: CHAT_GPT
        }
    }
}