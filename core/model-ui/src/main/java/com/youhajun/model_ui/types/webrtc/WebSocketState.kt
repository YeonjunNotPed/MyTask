package com.youhajun.model_ui.types.webrtc

import com.youhajun.model_data.types.WebSocketStateDto
import com.youhajun.model_ui.ToModel

sealed class WebSocketState {
    companion object : ToModel<WebSocketState, WebSocketStateDto> {

        override fun WebSocketStateDto.toModel(): WebSocketState {
            return when (this) {
                is WebSocketStateDto.Message -> Message(this.text)
                is WebSocketStateDto.Open -> Open
                is WebSocketStateDto.Failure -> Failure(this.t)
                is WebSocketStateDto.Close -> Close(this.code, this.reason)
            }
        }
    }

    data object Open : WebSocketState()
    data class Close(val code: Int, val reason: String) : WebSocketState()
    data class Failure(val t: Throwable) : WebSocketState()
    data class Message(val text: String) : WebSocketState()
}