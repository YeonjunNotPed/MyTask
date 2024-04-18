package com.youhajun.model_ui.vo.gpt

import com.youhajun.model_data.gpt.ChatGptMessageInfoResponse
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.vo.gpt.ChatGptMessageVo.Companion.toModel

data class ChatGptMessageInfoVo(
    val index: Int,
    val messageVo: ChatGptMessageVo,
    val finishReason: String,
) {
    companion object : ToModel<ChatGptMessageInfoVo, ChatGptMessageInfoResponse> {

        override fun ChatGptMessageInfoResponse.toModel(): ChatGptMessageInfoVo =
            ChatGptMessageInfoVo(
                index,
                messageDto.toModel(),
                finishReason
            )
    }
}