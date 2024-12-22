package com.youhajun.data.repositories

import com.youhajun.data.repositories.remoteDataSource.SignRemoteDataSource
import com.youhajun.model_data.login.LoginRequest
import com.youhajun.model_data.login.MyTaskToken
import com.youhajun.model_data.login.SocialLoginRequest
import com.youhajun.model_data.ApiResult
import javax.inject.Inject

class SignRepository @Inject constructor(
    private val signRemoteDataSource: SignRemoteDataSource
) {

    suspend fun postLogin(loginRequest: LoginRequest): ApiResult<MyTaskToken> =
        signRemoteDataSource.postLogin(loginRequest)

    suspend fun postSocialLogin(socialLoginRequest: SocialLoginRequest): ApiResult<MyTaskToken> =
        signRemoteDataSource.postSocialLogin(socialLoginRequest)
}

