package com.youhajun.data.services

import com.youhajun.data.Endpoint
import com.youhajun.data.model.dto.ApiResponse
import com.youhajun.data.model.dto.auth.MyTaskToken
import com.youhajun.data.model.dto.sign.LoginRequest
import com.youhajun.data.model.dto.sign.SocialLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface SignService {

    @POST(Endpoint.SIGN.POST_LOGIN)
    suspend fun postLogin(@Body request: LoginRequest): Response<ApiResponse<MyTaskToken>>

    @POST(Endpoint.SIGN.POST_LOGIN)
    suspend fun postSocialLogin(@Body socialLoginRequest: SocialLoginRequest): Response<ApiResponse<MyTaskToken>>
}