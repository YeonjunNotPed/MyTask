package com.youhajun.remote.services

import com.youhajun.model_data.ApiResponse
import com.youhajun.model_data.room.RoomPreviewInfo
import com.youhajun.remote.Endpoint
import retrofit2.Response
import retrofit2.http.GET

interface RoomService {

    @GET(Endpoint.Room.GET_ROOM_PREVIEW_INFO)
    suspend fun getRoomPreviewInfo(): Response<ApiResponse<RoomPreviewInfo>>
}