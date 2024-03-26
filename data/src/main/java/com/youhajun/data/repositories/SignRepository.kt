package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.models.dto.auth.MyTaskToken
import com.youhajun.data.models.dto.sign.LoginRequest
import com.youhajun.data.models.dto.sign.SocialLoginRequest
import com.youhajun.data.repositories.base.BaseRepository
import com.youhajun.data.repositories.remoteDataSource.SignRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class SignRepository @Inject constructor(
    private val signRemoteDataSource: SignRemoteDataSource
) : BaseRepository() {

    fun postLogin(loginRequest: LoginRequest): Flow<Resource<MyTaskToken>> =
        signRemoteDataSource.postLogin(loginRequest).map { myTaskApiConverter(it) }

    fun postSocialLogin(socialLoginRequest: SocialLoginRequest): Flow<Resource<MyTaskToken>> =
        signRemoteDataSource.postSocialLogin(socialLoginRequest).map { myTaskApiConverter(it) }
}

