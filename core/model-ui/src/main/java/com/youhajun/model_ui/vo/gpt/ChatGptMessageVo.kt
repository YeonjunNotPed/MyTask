package com.youhajun.model_ui.vo.gpt

import com.youhajun.model_data.gpt.ChatGptMessageRequest
import com.youhajun.model_data.gpt.ChatGptMessageResponse
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.types.gpt.GptRoleType

data class ChatGptMessageVo(
    val gptRoleType: GptRoleType,
    val content: String,
) {

    companion object : ToDto<ChatGptMessageVo, ChatGptMessageRequest>,
        ToModel<ChatGptMessageVo, ChatGptMessageResponse> {

        override fun ChatGptMessageVo.toDto(): ChatGptMessageRequest = ChatGptMessageRequest(
            gptRoleType.type, content
        )

        override fun ChatGptMessageResponse.toModel(): ChatGptMessageVo = ChatGptMessageVo(
            GptRoleType.typeOf(role), content
        )
    }
}