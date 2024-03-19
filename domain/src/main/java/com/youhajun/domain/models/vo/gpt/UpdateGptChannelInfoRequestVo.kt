package com.youhajun.domain.models.vo.gpt

import com.youhajun.data.models.dto.gpt.UpdateGptChannelInfoRequest
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.GptType

data class UpdateGptChannelInfoRequestVo(
    val idx:Long,
    val gptType: GptType,
    val roleOfAi: String?,
    val lastQuestion: String
) {
    companion object : Mapper.RequestMapper<UpdateGptChannelInfoRequest, UpdateGptChannelInfoRequestVo> {
        override fun mapModelToDto(type: UpdateGptChannelInfoRequestVo): UpdateGptChannelInfoRequest {
            return type.run {
                UpdateGptChannelInfoRequest(
                    idx,
                    gptType.type,
                    roleOfAi,
                    lastQuestion
                )
            }
        }

    }
}