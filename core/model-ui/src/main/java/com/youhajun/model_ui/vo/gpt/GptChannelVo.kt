package com.youhajun.model_ui.vo.gpt

import com.youhajun.model_data.gpt.GptChannelEntityDto
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.types.gpt.GptType

data class GptChannelVo(
    val channelIdx: Long = 0,
    val gptType: GptType,
    val roleOfAi: String? = null,
    val lastQuestion: String? = null,
    val createdAtUnixTimeStamp: Long,
) {
    companion object : ToDto<GptChannelVo, GptChannelEntityDto>, ToModel<GptChannelVo, GptChannelEntityDto> {
        override fun GptChannelVo.toDto(): GptChannelEntityDto = GptChannelEntityDto(
            channelIdx, gptType.type, roleOfAi, lastQuestion, createdAtUnixTimeStamp
        )

        override fun GptChannelEntityDto.toModel(): GptChannelVo = GptChannelVo(
            idx, GptType.typeOf(gptType), roleOfAi, lastQuestion, createdAtUnixTimeStamp
        )
    }
}