package com.youhajun.domain.model.sealeds

import com.youhajun.data.model.sealeds.WebSocketStateDTO
import com.youhajun.domain.model.Mapper

sealed class WebSocketState {
    companion object : Mapper.ResponseMapper<WebSocketStateDTO, WebSocketState> {
        override fun mapDtoToModel(type: WebSocketStateDTO): WebSocketState {
            return when (type) {
                is WebSocketStateDTO.Message -> Message(type.text)
                is WebSocketStateDTO.Close -> Open
                is WebSocketStateDTO.Failure -> Close
                is WebSocketStateDTO.Open -> Failure
            }
        }
    }

    object Open : WebSocketState()
    object Close : WebSocketState()
    object Failure : WebSocketState()
    data class Message(val text: String) : WebSocketState()
}