package com.youhajun.model_data.types

enum class SignalingCommandTypeDto(val type: String) {
    STATE("state"), // Command for WebRTCSessionState
    OFFER("offer"), // to send or receive offer
    ANSWER("answer"), // to send or receive answer
    ICE("ice"); // to send and receive ice candidates

    companion object {
        fun typeOf(value: String): SignalingCommandTypeDto {
            return entries.find {
                it.type == value
            } ?: STATE
        }
    }
}
