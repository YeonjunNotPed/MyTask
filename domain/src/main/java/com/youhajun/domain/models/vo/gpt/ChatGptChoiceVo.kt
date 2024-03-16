package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.dto.gpt.ChatGptMessageInfoResponse
import com.youhajun.domain.models.Mapper

data class ChatGptMessageInfoVo(
    val index: Int,
    val messageVo: ChatGptMessageVo,
    val finishReason: String,
) {
    companion object : Mapper.ResponseMapper<ChatGptMessageInfoResponse, ChatGptMessageInfoVo> {
        override fun mapDtoToModel(type: ChatGptMessageInfoResponse): ChatGptMessageInfoVo {
            return type.run {
                ChatGptMessageInfoVo(
                    index,
                    ChatGptMessageVo.mapDtoToModel(messageDto),
                    finishReason
                )
            }
        }
    }
}