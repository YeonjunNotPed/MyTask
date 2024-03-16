package com.youhajun.domain.models.enums

enum class GptRoleType(val type: String) {
    SYSTEM("system"),
    ASSISTANT("assistant"),
    USER("user");

    companion object {
        fun typeOf(value: String): GptRoleType {
            return values().find {
                it.type == value
            } ?: USER
        }
    }
}