package com.youhajun.model_ui.vo.gpt

import com.youhajun.model_data.gpt.GptMessageEntityDto
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.types.gpt.GptMessageFooterState
import com.youhajun.model_ui.types.gpt.GptMessageType

data class GptMessageVo(
    val idx: Long = 0,
    val channelIdx: Long,
    val gptMessageType: GptMessageType,
    val message: String,
    val createdAtUnixTimeStamp: Long,
    val messageFooterState: List<GptMessageFooterState> = emptyList(),
) {
    companion object : ToModel<GptMessageVo, GptMessageEntityDto>, ToDto<GptMessageVo, GptMessageEntityDto> {
        override fun GptMessageVo.toDto(): GptMessageEntityDto = GptMessageEntityDto(
            idx, channelIdx, gptMessageType.type, message, createdAtUnixTimeStamp
        )

        override fun GptMessageEntityDto.toModel(): GptMessageVo = GptMessageVo(
            idx, channelIdx, GptMessageType.typeOf(gptMessageType), message, createdAtUnixTimeStamp
        )
    }
}
