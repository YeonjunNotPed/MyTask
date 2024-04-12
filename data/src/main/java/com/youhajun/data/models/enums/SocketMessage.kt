package com.youhajun.data.models.enums

enum class SocketMessage(val type: String) {
    AUDIO_LEVELS("audio_levels");
    companion object {
        fun typeOf(value: String): SocketMessage {
            return values().find {
                it.type == value
            } ?: AUDIO_LEVELS
        }
    }
}