package com.youhajun.domain.models.enums

import com.youhajun.data.models.enums.WebRTCSessionState

enum class WebRTCSessionType(val type: String) {
    Active("active"),
    Creating("creating"),
    Ready("ready"),
    Impossible("impossible"),
    Offline("offline");

    companion object {
        fun typeOf(type: String): WebRTCSessionType {
            return WebRTCSessionType.values().find {
                it.type == type
            } ?: Offline
        }

        fun sessionStateOf(type: WebRTCSessionState): WebRTCSessionType {
            return when (type) {
                WebRTCSessionState.Active -> Active
                WebRTCSessionState.Creating -> Creating
                WebRTCSessionState.Ready -> Ready
                WebRTCSessionState.Impossible -> Impossible
                WebRTCSessionState.Offline -> Offline
            }
        }
    }
}
