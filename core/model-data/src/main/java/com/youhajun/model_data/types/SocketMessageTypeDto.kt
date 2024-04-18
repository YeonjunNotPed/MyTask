package com.youhajun.model_data.types

enum class SocketMessageTypeDto(val type: String) {
    AUDIO_LEVELS("audio_levels");
    companion object {
        fun typeOf(value: String): SocketMessageTypeDto {
            return entries.find {
                it.type == value
            } ?: AUDIO_LEVELS
        }
    }
}