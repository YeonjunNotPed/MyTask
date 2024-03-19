package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.entity.gpt.GptMessageEntity
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.GptMessageType
import com.youhajun.domain.models.sealeds.GptMessageFooterState

data class GptMessageVo(
    val idx: Long = 0,
    val channelIdx: Long,
    val gptMessageType: GptMessageType,
    val message: String,
    val createdAtUnixTimeStamp: Long,
    val messageFooterState: List<GptMessageFooterState> = emptyList(),
) {
    companion object : Mapper.ResponseMapper<GptMessageEntity, GptMessageVo>,
        Mapper.RequestMapper<GptMessageEntity, GptMessageVo> {
        override fun mapModelToDto(type: GptMessageVo): GptMessageEntity {
            return type.run {
                GptMessageEntity(
                    idx = idx,
                    channelIdx = channelIdx,
                    gptMessageType = gptMessageType.type,
                    message = message,
                    createdAtUnixTimeStamp = createdAtUnixTimeStamp
                )
            }
        }

        override fun mapDtoToModel(type: GptMessageEntity): GptMessageVo {
            return type.run {
                GptMessageVo(
                    idx = idx,
                    channelIdx = channelIdx,
                    gptMessageType = GptMessageType.typeOf(gptMessageType),
                    message = message,
                    createdAtUnixTimeStamp = createdAtUnixTimeStamp
                )
            }
        }
    }
}