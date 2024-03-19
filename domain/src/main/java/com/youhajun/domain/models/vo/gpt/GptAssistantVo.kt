package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.entity.gpt.GptAssistantEntity
import com.youhajun.domain.models.Mapper

data class GptAssistantVo(
    val idx: Long = 0,
    val channelIdx: Long,
    val assistantMessage: String,
    val createdAtUnixTimeStamp: Long,
) {
    companion object : Mapper.ResponseMapper<GptAssistantEntity, GptAssistantVo>, Mapper.RequestMapper<GptAssistantEntity, GptAssistantVo> {
        override fun mapDtoToModel(type: GptAssistantEntity): GptAssistantVo {
            return type.run {
                GptAssistantVo(
                    idx, channelIdx, assistantMessage, createdAtUnixTimeStamp
                )
            }
        }

        override fun mapModelToDto(type: GptAssistantVo): GptAssistantEntity {
            return type.run {
                GptAssistantEntity(
                    idx, channelIdx, assistantMessage, createdAtUnixTimeStamp
                )
            }
        }
    }
}