package com.youhajun.domain

import com.youhajun.data.onSuccess
import com.youhajun.data.repositories.AuthRepository
import com.youhajun.data.repositories.SignRepository
import com.youhajun.model_data.ApiResult
import com.youhajun.model_data.login.MyTaskToken
import com.youhajun.model_data.login.SocialLoginRequest
import javax.inject.Inject

class PostSocialLoginUseCase @Inject constructor(
    private val signRepository: SignRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: SocialLoginRequest): ApiResult<MyTaskToken> {
        return signRepository.postSocialLogin(request).onSuccess {
            authRepository.saveMyTaskToken(it)
        }
    }
}
