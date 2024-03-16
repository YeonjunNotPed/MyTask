package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.dto.gpt.ChatGptRequest
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.GptType

data class ChatGptRequestVo(
    val model: GptType,
    val messageList : List<ChatGptMessageVo>,
) {
    companion object : Mapper.RequestMapper<ChatGptRequest, ChatGptRequestVo> {
        override fun mapModelToDto(type: ChatGptRequestVo): ChatGptRequest {
            return type.run {
                ChatGptRequest(
                    messages = messageList.map { ChatGptMessageVo.mapModelToDto(it) },
                    model.type,
                )
            }
        }
    }
}