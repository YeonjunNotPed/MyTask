package com.youhajun.model_ui.vo.room

import com.youhajun.model_data.room.RoomPreview
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.types.room.RoomType

data class RoomPreviewVo(
    val idx: Long,
    val roomType: RoomType,
    val title: String,
    val content: String,
    val createAt: String,
    val matchingStandardNumber: Int,
    val currentPeopleNumber: Int,
) {
    companion object : ToModel<RoomPreviewVo, RoomPreview> {
        override fun RoomPreview.toModel(): RoomPreviewVo = RoomPreviewVo(
            idx, RoomType.typeOf(roomType), title, content, createAt, matchingStandardNumber, currentPeopleNumber
        )
    }
}