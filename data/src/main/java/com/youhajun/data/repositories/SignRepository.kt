package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.model.dto.auth.MyTaskToken
import com.youhajun.data.model.dto.sign.LoginRequest
import com.youhajun.data.model.dto.sign.SocialLoginRequest
import com.youhajun.data.repositories.base.BaseRepository
import com.youhajun.data.repositories.remoteDataSource.SignRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SignRepository @Inject constructor(
    private val signRemoteDataSource: SignRemoteDataSource
) : BaseRepository() {

    fun postLogin(loginRequest: LoginRequest): Flow<Resource<MyTaskToken>> =
        signRemoteDataSource.postLogin(loginRequest).map { apiConverter(it) }

    fun postSocialLogin(socialLoginRequest: SocialLoginRequest): Flow<Resource<MyTaskToken>> =
        signRemoteDataSource.postSocialLogin(socialLoginRequest).map { apiConverter(it) }
}

