package com.youhajun.model_ui.types.webrtc

import com.youhajun.model_data.types.SocketMessageTypeDto
import com.youhajun.model_ui.ToDto

enum class SocketMessageType(val type: String) {
    AUDIO_LEVELS("audio_levels");
    companion object: ToDto<SocketMessageType, SocketMessageTypeDto> {

        override fun SocketMessageType.toDto(): SocketMessageTypeDto {
            return SocketMessageTypeDto.valueOf(this.name)
        }

        fun typeOf(value: String): SocketMessageType {
            return entries.find {
                it.type == value
            } ?: AUDIO_LEVELS
        }
    }
}