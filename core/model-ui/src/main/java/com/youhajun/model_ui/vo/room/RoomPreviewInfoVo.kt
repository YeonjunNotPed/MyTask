package com.youhajun.model_ui.vo.room

import com.youhajun.model_data.room.RoomPreviewInfo
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.vo.room.RoomPreviewVo.Companion.toModel

data class RoomPreviewInfoVo(
    val roomList: List<RoomPreviewVo>,
    val pollingTime: Long
) {
    companion object : ToModel<RoomPreviewInfoVo, RoomPreviewInfo> {

        override fun RoomPreviewInfo.toModel(): RoomPreviewInfoVo = RoomPreviewInfoVo(
            roomList.map { it.toModel() },
            pollingTime
        )
    }
}