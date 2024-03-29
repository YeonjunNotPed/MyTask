package com.youhajun.ui.models.states

import com.youhajun.domain.models.vo.room.RoomPreviewVo

data class SelectRoomState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val roomList: List<RoomPreviewVo> = emptyList()
)