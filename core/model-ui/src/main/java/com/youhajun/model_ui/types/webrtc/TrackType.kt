package com.youhajun.model_ui.types.webrtc

enum class TrackType(val type: String) {
    VIDEO("VIDEO"),
    AUDIO("AUDIO"),
    SCREEN_SHARE_VIDEO("SCREEN_SHARE"),
    SCREEN_SHARE_AUDIO("SCREEN_SHARE_AUDIO");

    companion object {
        fun typeOf(value: String): TrackType {
            return entries.find {
                it.type == value
            } ?: AUDIO
        }
    }
}