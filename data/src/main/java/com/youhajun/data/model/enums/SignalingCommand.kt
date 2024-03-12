package com.youhajun.data.model.enums

enum class SignalingCommand(val type: String) {
    STATE("state"), // Command for WebRTCSessionState
    OFFER("offer"), // to send or receive offer
    ANSWER("answer"), // to send or receive answer
    ICE("ice"); // to send and receive ice candidates

    companion object {
        fun typeOf(value: String): SignalingCommand {
            return values().find {
                it.type == value
            } ?: STATE
        }
    }
}
