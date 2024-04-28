package com.youhajun.model_ui.states

import com.youhajun.model_ui.vo.room.RoomPreviewVo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SelectRoomState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val roomList: ImmutableList<RoomPreviewVo> = persistentListOf()
)