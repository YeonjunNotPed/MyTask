package com.youhajun.domain.models.enums

enum class GptType(val type: String) {
    CHAT_GPT_3_5_TURBO("gpt-3.5-turbo"),
    CHAT_GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),
    GEMINI("gemini"),
    NONE("none");

    companion object {
        fun typeOf(value: String): GptType {
            return values().find {
                it.type == value
            } ?: NONE
        }
    }
}