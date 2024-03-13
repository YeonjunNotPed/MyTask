package com.youhajun.domain.models.enums

enum class GptMessageType(val type: String) {
    ANSWER("answer"), QUESTION("question");

    companion object {
        fun typeOf(value: String): GptMessageType {
            return values().find {
                it.type == value
            } ?: ANSWER
        }
    }
}