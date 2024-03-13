package com.youhajun.data.services

import com.youhajun.data.Endpoint
import com.youhajun.data.models.dto.ApiResponse
import com.youhajun.data.models.dto.auth.MyTaskToken
import com.youhajun.data.models.dto.sign.LoginRequest
import com.youhajun.data.models.dto.sign.SocialLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignService {

    @POST(Endpoint.Sign.POST_LOGIN)
    suspend fun postLogin(@Body request: LoginRequest): Response<ApiResponse<MyTaskToken>>

    @POST(Endpoint.Sign.POST_LOGIN)
    suspend fun postSocialLogin(@Body socialLoginRequest: SocialLoginRequest): Response<ApiResponse<MyTaskToken>>
}