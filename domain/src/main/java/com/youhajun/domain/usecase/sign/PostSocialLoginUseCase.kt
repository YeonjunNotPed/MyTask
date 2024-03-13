package com.youhajun.domain.usecase.sign

import com.youhajun.data.Resource
import com.youhajun.data.repositories.AuthRepository
import com.youhajun.data.repositories.SignRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.SocialLoginRequestVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostSocialLoginUseCase @Inject constructor(
    private val signRepository: SignRepository,
    private val authRepository: AuthRepository
) : UseCase<SocialLoginRequestVo, Flow<UiState<Unit>>>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun invoke(request: SocialLoginRequestVo): Flow<UiState<Unit>> {
        return signRepository.postSocialLogin(SocialLoginRequestVo.mapModelToDto(request)).flatMapConcat {
            when(it) {
                is Resource.Success -> authRepository.saveMyTaskToken(it.data)
                is Resource.Error -> flow { emit(Resource.Error(it.errorVo.convert())) }
                is Resource.Loading -> emptyFlow()
            }
        }.map {
            it.mapToUiState { }
        }
    }
}