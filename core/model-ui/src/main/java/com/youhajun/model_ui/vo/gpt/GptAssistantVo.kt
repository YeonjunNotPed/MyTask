package com.youhajun.model_ui.vo.gpt

import com.youhajun.model_data.gpt.GptAssistantEntityDto
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.ToModel

data class GptAssistantVo(
    val idx: Long = 0,
    val channelIdx: Long,
    val assistantMessage: String,
    val createdAtUnixTimeStamp: Long,
) {
    companion object : ToModel<GptAssistantVo, GptAssistantEntityDto>,
        ToDto<GptAssistantVo, GptAssistantEntityDto> {

        override fun GptAssistantVo.toDto(): GptAssistantEntityDto = GptAssistantEntityDto(
            idx, channelIdx, assistantMessage, createdAtUnixTimeStamp
        )

        override fun GptAssistantEntityDto.toModel(): GptAssistantVo = GptAssistantVo(
            idx, channelIdx, assistantMessage, createdAtUnixTimeStamp
        )
    }
}