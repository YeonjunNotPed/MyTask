package com.youhajun.model_ui.vo.gpt

import com.youhajun.model_data.gpt.ChatGptRequest
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.types.gpt.GptType
import com.youhajun.model_ui.vo.gpt.ChatGptMessageVo.Companion.toDto

data class ChatGptRequestVo(
    val type: GptType,
    val messageList: List<ChatGptMessageVo>,
) {
    companion object : ToDto<ChatGptRequestVo, ChatGptRequest> {
        override fun ChatGptRequestVo.toDto(): ChatGptRequest = ChatGptRequest(
            messageList.map { it.toDto() },
            type.type,
        )
    }
}