package com.youhajun.data.services

import com.youhajun.data.Endpoint
import com.youhajun.data.model.dto.ApiResponse
import com.youhajun.data.model.dto.auth.MyTaskToken
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService {

    companion object {
        private const val QUERY_REFRESH_TOKEN = "refreshToken"
    }

    @GET(Endpoint.AUTH.GET_REFRESHED_TOKEN)
    suspend fun getRefreshedToken(@Query(QUERY_REFRESH_TOKEN) refreshToken: String): Response<ApiResponse<MyTaskToken>>
}