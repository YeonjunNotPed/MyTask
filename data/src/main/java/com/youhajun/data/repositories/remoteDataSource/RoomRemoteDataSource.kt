package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.data.models.dto.ApiResponse
import com.youhajun.data.models.dto.room.RoomPreviewInfo
import com.youhajun.data.network.safeResponseFlow
import com.youhajun.data.services.RoomService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class RoomRemoteDataSource @Inject constructor(
    private val roomService: RoomService,
) {
    suspend fun getRoomPreviewInfo(): Flow<Response<ApiResponse<RoomPreviewInfo>>> = flow {
        emit(roomService.getRoomPreviewInfo())
    }.safeResponseFlow()
}