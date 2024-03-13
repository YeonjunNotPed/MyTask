package com.youhajun.data.models.enums

enum class WebRTCSessionState(val type:String) {
  Active("active"), // Offer and Answer messages has been sent
  Creating("creating"), // Creating session, offer has been sent
  Ready("ready"), // Both clients available and ready to initiate session
  Impossible("impossible"), // We have less than two clients connected to the server
  Offline("offline"); // unable to connect signaling server
}
