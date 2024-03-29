package com.youhajun.domain.models.sealeds

import com.youhajun.data.models.MyTaskCode
import com.youhajun.data.models.sealeds.WebSocketStateDTO
import com.youhajun.domain.models.Mapper

sealed class WebSocketState {
    companion object : Mapper.ResponseMapper<WebSocketStateDTO, WebSocketState> {
        override fun mapDtoToModel(type: WebSocketStateDTO): WebSocketState {
            return when (type) {
                is WebSocketStateDTO.Message -> Success.Message(type.text)
                is WebSocketStateDTO.Open -> Success.Open
                is WebSocketStateDTO.Failure -> Error.Failure
                is WebSocketStateDTO.Close -> if(type.code == MyTaskCode.WEB_SOCKET_SUCCESS_CODE) {
                    Success.Close
                } else Error.Close
            }
        }
    }

    sealed class Success: WebSocketState() {
        object Open : Success()
        data class Message(val text: String) : Success()
        object Close : Success()
    }

    sealed class Error: WebSocketState() {
        object Failure : Error()
        object Close : Error()
    }
}