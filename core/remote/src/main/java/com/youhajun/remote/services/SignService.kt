package com.youhajun.remote.services

import com.youhajun.model_data.ApiResponse
import com.youhajun.model_data.login.LoginRequest
import com.youhajun.model_data.login.MyTaskToken
import com.youhajun.model_data.login.SocialLoginRequest
import com.youhajun.remote.Endpoint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignService {

    @POST(Endpoint.Sign.POST_LOGIN)
    suspend fun postLogin(@Body request: LoginRequest): Response<ApiResponse<MyTaskToken>>

    @POST(Endpoint.Sign.POST_LOGIN)
    suspend fun postSocialLogin(@Body socialLoginRequest: SocialLoginRequest): Response<ApiResponse<MyTaskToken>>
}