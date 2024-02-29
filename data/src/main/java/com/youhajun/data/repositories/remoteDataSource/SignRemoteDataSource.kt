package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.data.model.dto.ApiResponse
import com.youhajun.data.model.dto.auth.MyTaskToken
import com.youhajun.data.model.dto.sign.LoginRequest
import com.youhajun.data.model.dto.sign.SocialLoginRequest
import com.youhajun.data.network.safeFlow
import com.youhajun.data.services.SignService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignRemoteDataSource @Inject constructor(
    private val signService: SignService
) {
    fun postLogin(loginRequest: LoginRequest): Flow<Response<ApiResponse<MyTaskToken>>> = flow {
        emit(signService.postLogin(loginRequest))
    }.safeFlow()

    fun postSocialLogin(socialLoginRequest: SocialLoginRequest): Flow<Response<ApiResponse<MyTaskToken>>> = flow {
        emit(signService.postSocialLogin(socialLoginRequest))
    }.safeFlow()
}