package com.youhajun.domain.models.enums

enum class SocketMessageType(val type: String) {
    AUDIO_LEVELS("audio_levels");
    companion object {
        fun typeOf(value: String): SocketMessageType {
            return values().find {
                it.type == value
            } ?: AUDIO_LEVELS
        }
    }
}