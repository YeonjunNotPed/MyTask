package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.dto.gpt.ChatGptResponse
import com.youhajun.domain.models.Mapper

data class ChatGptResponseVo(
    val id: String,
    val message: ChatGptMessageVo,
    val createAt: Int,
    val model:String
) {
    companion object : Mapper.ResponseMapper<ChatGptResponse, ChatGptResponseVo> {
        override fun mapDtoToModel(type: ChatGptResponse): ChatGptResponseVo {
            return type.run {
                ChatGptResponseVo(
                    id = id,
                    message = ChatGptChoiceVo.mapDtoToModel(choices.first()).messageVo,
                    createAt = created,
                    model = model
                )
            }
        }
    }
}