package com.youhajun.domain.models.enums

enum class WebRTCSessionType(val type:String) {
  Active("active"),
  Creating("creating"),
  Ready("ready"),
  Impossible("impossible"),
  Offline("offline");

  companion object {
    fun typeOf(value: String): WebRTCSessionType {
      return WebRTCSessionType.values().find {
        it.type == value
      } ?: Active
    }
  }
}
