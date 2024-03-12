package com.youhajun.data.model.sealeds

import okhttp3.Response

sealed class WebSocketStateDTO {
  data class Open(val response: Response) : WebSocketStateDTO()
  data class Close(val code: Int, val reason: String) : WebSocketStateDTO()
  data class Failure(val t: Throwable, val response: Response?) : WebSocketStateDTO()
  data class Message(val text: String) : WebSocketStateDTO()
}