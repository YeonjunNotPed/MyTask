package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.dto.gpt.ChatGptMessageDto
import com.youhajun.domain.models.Mapper

data class ChatGptMessageVo(
    val role: String,
    val content: String,
) {

    companion object : Mapper.RequestMapper<ChatGptMessageDto, ChatGptMessageVo>, Mapper.ResponseMapper<ChatGptMessageDto, ChatGptMessageVo> {
        override fun mapModelToDto(type: ChatGptMessageVo): ChatGptMessageDto {
            return type.run {
                ChatGptMessageDto(
                    role, content
                )
            }
        }

        override fun mapDtoToModel(type: ChatGptMessageDto): ChatGptMessageVo {
            return type.run {
                ChatGptMessageVo(
                    role, content
                )
            }
        }
    }
}