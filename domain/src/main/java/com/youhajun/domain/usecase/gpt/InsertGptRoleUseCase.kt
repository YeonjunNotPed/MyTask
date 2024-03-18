package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.gpt.ChatGptRequestVo
import com.youhajun.domain.models.vo.gpt.ChatGptResponseVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InsertGptRoleUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<String, Flow<UiState<Unit>>>() {

    override suspend fun invoke(request: String): Flow<UiState<Unit>> {
        return gptRepository.insertGptRole(request).map {
            it.mapToUiState {  }
        }
    }
}