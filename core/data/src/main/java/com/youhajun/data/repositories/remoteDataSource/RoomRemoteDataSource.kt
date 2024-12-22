package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.model_data.room.RoomPreviewInfo
import com.youhajun.model_data.ApiResult
import com.youhajun.remote.myTaskApiHandle
import com.youhajun.remote.services.RoomService
import javax.inject.Inject

class RoomRemoteDataSource @Inject constructor(
    private val roomService: RoomService,
) {
    suspend fun getRoomPreviewInfo(): ApiResult<RoomPreviewInfo> =
        myTaskApiHandle { roomService.getRoomPreviewInfo() }
}