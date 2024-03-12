package com.youhajun.ui.utils.webRtc.models

enum class TrackType(val type: String) {
    VIDEO("VIDEO"),
    AUDIO("AUDIO"),
    SCREEN_SHARE("SCREEN_SHARE"),
    SCREEN_SHARE_AUDIO("SCREEN_SHARE_AUDIO");

    companion object {
        fun typeOf(value: String): TrackType {
            return TrackType.values().find {
                it.type == value
            } ?: AUDIO
        }
    }
}