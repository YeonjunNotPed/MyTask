package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.data.model.dto.ApiResponse
import com.youhajun.data.model.dto.auth.MyTaskToken
import com.youhajun.data.network.safeFlow
import com.youhajun.data.services.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService
) {
    fun getRefreshedToken(refreshToken: String): Flow<Response<ApiResponse<MyTaskToken>>> = flow {
        emit(authService.getRefreshedToken(refreshToken))
    }.safeFlow()
}