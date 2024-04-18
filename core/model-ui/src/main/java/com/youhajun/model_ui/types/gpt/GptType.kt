package com.youhajun.model_ui.types.gpt

enum class GptType(val type: String) {
    CHAT_GPT_3_5_TURBO("gpt-3.5-turbo"),
    CHAT_GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),
    GEMINI("gemini"),
    NONE("none");

    fun toGptAiType(): GptAIType {
        return when (this) {
            CHAT_GPT_3_5_TURBO,
            CHAT_GPT_4_TURBO_PREVIEW -> GptAIType.CHAT_GPT
            GEMINI -> GptAIType.GEMINI
            NONE -> GptAIType.NONE
        }
    }

    companion object {
        fun typeOf(value: String): GptType {
            return entries.find {
                it.type == value
            } ?: NONE
        }
    }
}