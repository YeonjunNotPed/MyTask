package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.model_data.login.MyTaskToken
import com.youhajun.model_data.ApiResult
import com.youhajun.remote.myTaskApiHandle
import com.youhajun.remote.services.AuthService
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService
) {
    suspend fun getRefreshedToken(refreshToken: String): ApiResult<MyTaskToken> =
        myTaskApiHandle { authService.getRefreshedToken(refreshToken) }
}