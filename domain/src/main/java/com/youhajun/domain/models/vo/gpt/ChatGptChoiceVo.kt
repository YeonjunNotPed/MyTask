package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.dto.gpt.ChatGptChoiceDto
import com.youhajun.domain.models.Mapper

data class ChatGptChoiceVo(
    val index: Int,
    val messageVo: ChatGptMessageVo,
) {
    companion object : Mapper.ResponseMapper<ChatGptChoiceDto, ChatGptChoiceVo> {
        override fun mapDtoToModel(type: ChatGptChoiceDto): ChatGptChoiceVo {
            return type.run {
                ChatGptChoiceVo(
                    index,
                    ChatGptMessageVo.mapDtoToModel(messageDto)
                )
            }
        }
    }
}