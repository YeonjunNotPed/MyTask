package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.data.models.dto.ApiResponse
import com.youhajun.data.models.dto.auth.MyTaskToken
import com.youhajun.data.models.dto.sign.LoginRequest
import com.youhajun.data.models.dto.sign.SocialLoginRequest
import com.youhajun.data.network.safeResponseFlow
import com.youhajun.data.services.SignService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class SignRemoteDataSource @Inject constructor(
    private val signService: SignService
) {
    fun postLogin(loginRequest: LoginRequest): Flow<Response<ApiResponse<MyTaskToken>>> = flow {
        emit(signService.postLogin(loginRequest))
    }.safeResponseFlow()

    fun postSocialLogin(socialLoginRequest: SocialLoginRequest): Flow<Response<ApiResponse<MyTaskToken>>> = flow {
        emit(signService.postSocialLogin(socialLoginRequest))
    }.safeResponseFlow()
}