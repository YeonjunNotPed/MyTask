package com.youhajun.domain.models.enums

enum class GptAiType(val type: String) {
    NONE("none"), CHAT_GPT("chat_gpt"), GEMINI("gemini");

    companion object {
        fun typeOf(type: String): GptAiType {
            return GptAiType.values().find {
                it.type == type
            } ?: NONE
        }

        fun gptTypeOf(gptType: GptType): GptAiType {
            return when (gptType) {
                GptType.CHAT_GPT_3_5_TURBO,
                GptType.CHAT_GPT_4_TURBO_PREVIEW -> CHAT_GPT
                GptType.GEMINI -> GEMINI
                GptType.NONE -> NONE
            }
        }
    }
}
