package com.youhajun.model_ui.types.gpt

enum class GptRoleType(val type: String) {
    SYSTEM("system"),
    ASSISTANT("assistant"),
    USER("user");

    companion object {
        fun typeOf(value: String): GptRoleType {
            return entries.find {
                it.type == value
            } ?: USER
        }
    }
}