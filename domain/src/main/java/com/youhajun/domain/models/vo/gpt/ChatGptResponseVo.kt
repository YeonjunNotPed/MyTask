package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.dto.gpt.ChatGptResponse
import com.youhajun.domain.models.Mapper

data class ChatGptResponseVo(
    val id: String,
    val message: List<ChatGptMessageInfoVo>,
    val createdAtUnixTimestamp: Long,
    val model:String
) {
    companion object : Mapper.ResponseMapper<ChatGptResponse, ChatGptResponseVo> {
        override fun mapDtoToModel(type: ChatGptResponse): ChatGptResponseVo {
            return type.run {
                ChatGptResponseVo(
                    id = id,
                    message = messageList.map { ChatGptMessageInfoVo.mapDtoToModel(it) },
                    createdAtUnixTimestamp = created,
                    model = model
                )
            }
        }
    }
}