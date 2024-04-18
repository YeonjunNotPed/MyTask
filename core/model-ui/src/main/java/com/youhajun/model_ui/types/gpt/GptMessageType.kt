package com.youhajun.model_ui.types.gpt

enum class GptMessageType(val type: String) {
    ANSWER("answer"), QUESTION("question");

    companion object {
        fun typeOf(value: String): GptMessageType {
            return entries.find {
                it.type == value
            } ?: ANSWER
        }
    }
}