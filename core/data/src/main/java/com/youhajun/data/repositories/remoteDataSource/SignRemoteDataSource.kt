package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.model_data.login.LoginRequest
import com.youhajun.model_data.login.MyTaskToken
import com.youhajun.model_data.login.SocialLoginRequest
import com.youhajun.model_data.ApiResult
import com.youhajun.remote.myTaskApiHandle
import com.youhajun.remote.services.SignService
import javax.inject.Inject

class SignRemoteDataSource @Inject constructor(
    private val signService: SignService
) {
    suspend fun postLogin(loginRequest: LoginRequest): ApiResult<MyTaskToken> =
        myTaskApiHandle { signService.postLogin(loginRequest) }

    suspend fun postSocialLogin(socialLoginRequest: SocialLoginRequest): ApiResult<MyTaskToken> =
        myTaskApiHandle { signService.postSocialLogin(socialLoginRequest) }
}