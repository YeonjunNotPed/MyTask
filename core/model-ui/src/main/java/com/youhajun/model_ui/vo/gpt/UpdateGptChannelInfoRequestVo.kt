package com.youhajun.model_ui.vo.gpt

import com.youhajun.model_data.gpt.UpdateGptChannelInfoRequest
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.types.gpt.GptType

data class UpdateGptChannelInfoRequestVo(
    val idx: Long,
    val gptType: GptType,
    val roleOfAi: String?,
    val lastQuestion: String
) {
    companion object : ToDto<UpdateGptChannelInfoRequestVo, UpdateGptChannelInfoRequest> {

        override fun UpdateGptChannelInfoRequestVo.toDto(): UpdateGptChannelInfoRequest =
            UpdateGptChannelInfoRequest(
                idx,
                gptType.type,
                roleOfAi,
                lastQuestion
            )
    }
}