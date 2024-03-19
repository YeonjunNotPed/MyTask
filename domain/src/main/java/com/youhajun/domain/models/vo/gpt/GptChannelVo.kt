package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.entity.gpt.GptChannelEntity
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.GptType

data class GptChannelVo(
    val channelIdx: Long = 0,
    val gptType: GptType,
    val roleOfAi: String? = null,
    val lastQuestion: String? = null,
    val createdAtUnixTimeStamp: Long,
) {
    companion object : Mapper.ResponseMapper<GptChannelEntity, GptChannelVo>, Mapper.RequestMapper<GptChannelEntity, GptChannelVo> {
        override fun mapDtoToModel(type: GptChannelEntity): GptChannelVo {
            return type.run {
                GptChannelVo(
                    channelIdx = idx,
                    gptType = GptType.typeOf(gptType),
                    roleOfAi = roleOfAi,
                    lastQuestion = lastQuestion,
                    createdAtUnixTimeStamp = createdAtUnixTimeStamp
                )
            }
        }

        override fun mapModelToDto(type: GptChannelVo): GptChannelEntity {
            return type.run {
                GptChannelEntity(
                    idx = channelIdx,
                    gptType = gptType.type,
                    roleOfAi = roleOfAi,
                    lastQuestion = lastQuestion,
                    createdAtUnixTimeStamp = createdAtUnixTimeStamp
                )
            }
        }

    }
}