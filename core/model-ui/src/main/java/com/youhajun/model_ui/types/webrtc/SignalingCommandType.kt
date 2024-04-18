package com.youhajun.model_ui.types.webrtc

import com.youhajun.model_data.types.SignalingCommandTypeDto
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.ToModel

enum class SignalingCommandType(val type: String) {
    STATE("state"),
    OFFER("offer"),
    ANSWER("answer"),
    ICE("ice");
    companion object : ToDto<SignalingCommandType, SignalingCommandTypeDto>, ToModel<SignalingCommandType, SignalingCommandTypeDto> {

        override fun SignalingCommandTypeDto.toModel(): SignalingCommandType {
            return SignalingCommandType.valueOf(this.name)
        }

        override fun SignalingCommandType.toDto(): SignalingCommandTypeDto {
            return SignalingCommandTypeDto.valueOf(this.name)
        }

        fun typeOf(value: String): SignalingCommandType {
            return entries.find {
                it.type == value
            } ?: STATE
        }
    }
}