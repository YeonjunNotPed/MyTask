package com.youhajun.model_data.types

import okhttp3.Response

sealed class WebSocketStateDto {
  data class Open(val response: Response) : WebSocketStateDto()
  data class Close(val code: Int, val reason: String) : WebSocketStateDto()
  data class Failure(val t: Throwable, val response: Response?) : WebSocketStateDto()
  data class Message(val text: String) : WebSocketStateDto()
}