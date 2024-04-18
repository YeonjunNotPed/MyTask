package com.youhajun.model_ui.types.gpt

enum class GptAIType(val type: String) {
    NONE("none"), CHAT_GPT("chat_gpt"), GEMINI("gemini");

    companion object {
        fun typeOf(type: String): GptAIType {
            return entries.find {
                it.type == type
            } ?: NONE
        }
    }
}
