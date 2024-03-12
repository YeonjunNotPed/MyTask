package com.youhajun.ui.models.states

import com.youhajun.domain.model.vo.RoomPreviewVo

data class SelectRoomState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val roomList: List<RoomPreviewVo> = emptyList()
)