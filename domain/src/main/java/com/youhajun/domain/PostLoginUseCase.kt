package com.youhajun.domain

import com.youhajun.data.onSuccess
import com.youhajun.data.repositories.AuthRepository
import com.youhajun.data.repositories.SignRepository
import com.youhajun.model_data.ApiResult
import com.youhajun.model_data.login.LoginRequest
import com.youhajun.model_data.login.MyTaskToken
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(
    private val signRepository: SignRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(request: LoginRequest): ApiResult<MyTaskToken> {
        return signRepository.postLogin(request).onSuccess {
            authRepository.saveMyTaskToken(it)
        }
    }
}
