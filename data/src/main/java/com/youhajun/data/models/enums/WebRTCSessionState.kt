package com.youhajun.data.models.enums

enum class WebRTCSessionState(val type:String) {
  Active("Active"), // Offer and Answer messages has been sent
  Creating("Creating"), // Creating session, offer has been sent
  Ready("Ready"), // Both clients available and ready to initiate session
  Impossible("Impossible"), // We have less than two clients connected to the server
  Offline("Offline"); // unable to connect signaling server
  companion object {
    fun typeOf(value: String): WebRTCSessionState {
      return WebRTCSessionState.values().find {
        it.type == value
      } ?: Offline
    }
  }
}
