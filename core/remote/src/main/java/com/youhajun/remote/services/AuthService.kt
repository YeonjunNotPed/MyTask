package com.youhajun.remote.services

import com.youhajun.model_data.ApiResponse
import com.youhajun.model_data.login.MyTaskToken
import com.youhajun.remote.Endpoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService {

    companion object {
        private const val QUERY_REFRESH_TOKEN = "refreshToken"
    }

    @GET(Endpoint.Auth.GET_REFRESHED_TOKEN)
    suspend fun getRefreshedToken(@Query(QUERY_REFRESH_TOKEN) refreshToken: String): Response<ApiResponse<MyTaskToken>>
}