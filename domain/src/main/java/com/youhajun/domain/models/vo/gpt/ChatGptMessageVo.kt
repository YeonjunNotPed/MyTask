package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.dto.gpt.ChatGptMessageRequest
import com.youhajun.data.models.dto.gpt.ChatGptMessageResponse
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.GptRoleType

data class ChatGptMessageVo(
    val gptRoleType: GptRoleType,
    val content: String,
) {

    companion object : Mapper.RequestMapper<ChatGptMessageRequest, ChatGptMessageVo>, Mapper.ResponseMapper<ChatGptMessageResponse, ChatGptMessageVo> {
        override fun mapModelToDto(type: ChatGptMessageVo): ChatGptMessageRequest {
            return type.run {
                ChatGptMessageRequest(
                    gptRoleType.type, content
                )
            }
        }

        override fun mapDtoToModel(type: ChatGptMessageResponse): ChatGptMessageVo {
            return type.run {
                ChatGptMessageVo(
                    GptRoleType.typeOf(role), content
                )
            }
        }
    }
}