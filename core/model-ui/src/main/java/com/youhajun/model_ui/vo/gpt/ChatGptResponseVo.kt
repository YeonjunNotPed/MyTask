package com.youhajun.model_ui.vo.gpt

import com.youhajun.model_data.gpt.ChatGptResponse
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.vo.gpt.ChatGptMessageInfoVo.Companion.toModel

data class ChatGptResponseVo(
    val id: String,
    val message: List<ChatGptMessageInfoVo>,
    val createdAtUnixTimestamp: Long,
    val model: String
) {
    companion object : ToModel<ChatGptResponseVo, ChatGptResponse> {
        override fun ChatGptResponse.toModel(): ChatGptResponseVo = ChatGptResponseVo(
            id,
            messageList.map { it.toModel() },
            created,
            model
        )
    }
}