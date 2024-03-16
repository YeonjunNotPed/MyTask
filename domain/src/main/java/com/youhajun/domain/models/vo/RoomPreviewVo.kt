package com.youhajun.domain.models.vo

import com.youhajun.data.models.dto.room.RoomPreview
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.RoomType

data class RoomPreviewVo(
    val idx: Long,
    val roomType: RoomType,
    val title: String,
    val content: String,
    val createAt: String,
    val matchingStandardNumber: Int,
    val currentPeopleNumber: Int,
) {
    companion object : Mapper.ResponseMapper<RoomPreview, RoomPreviewVo> {
        override fun mapDtoToModel(type: RoomPreview): RoomPreviewVo {
            return type.run {
                RoomPreviewVo(
                    idx = idx,
                    roomType = RoomType.typeOf(roomType),
                    title = title,
                    content = content,
                    createAt = createAt,
                    matchingStandardNumber = matchingStandardNumber,
                    currentPeopleNumber = currentPeopleNumber
                )
            }
        }
    }
}