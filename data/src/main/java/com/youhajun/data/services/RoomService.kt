package com.youhajun.data.services

import com.youhajun.data.Endpoint
import com.youhajun.data.model.dto.ApiResponse
import com.youhajun.data.model.dto.room.RoomPreviewInfo
import retrofit2.Response
import retrofit2.http.GET

interface RoomService {

    @GET(Endpoint.Room.GET_ROOM_PREVIEW_INFO)
    suspend fun getRoomPreviewInfo(): Response<ApiResponse<RoomPreviewInfo>>
}