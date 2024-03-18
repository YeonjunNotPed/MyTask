package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.entity.gpt.GptAssistantEntity
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.GptType

data class GptAssistantVo(
    val assistantIdx: Long = 0,
    val gptType: GptType,
    val lastQuestion: String? = null,
    val createdAtUnixTimeStamp: Long,
) {
    companion object : Mapper.ResponseMapper<GptAssistantEntity, GptAssistantVo>, Mapper.RequestMapper<GptAssistantEntity, GptAssistantVo> {
        override fun mapDtoToModel(type: GptAssistantEntity): GptAssistantVo {
            return type.run {
                GptAssistantVo(
                    assistantIdx = idx,
                    gptType = GptType.typeOf(gptType),
                    lastQuestion = lastQuestion,
                    createdAtUnixTimeStamp = createdAtUnixTimeStamp
                )
            }
        }

        override fun mapModelToDto(type: GptAssistantVo): GptAssistantEntity {
            return type.run {
                GptAssistantEntity(
                    idx = assistantIdx,
                    gptType = gptType.type,
                    lastQuestion = lastQuestion,
                    createdAtUnixTimeStamp = createdAtUnixTimeStamp
                )
            }
        }

    }
}