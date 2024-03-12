package com.youhajun.domain.model.enums

enum class SignalingType(val type: String) {
    STATE("state"),
    OFFER("offer"),
    ANSWER("answer"),
    ICE("ice");
    companion object {
        fun typeOf(value: String): SignalingType {
            return values().find {
                it.type == value
            } ?: STATE
        }
    }
}