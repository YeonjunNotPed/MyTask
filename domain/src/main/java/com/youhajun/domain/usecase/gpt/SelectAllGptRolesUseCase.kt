package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelectAllGptRolesUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<Unit, Flow<UiState<List<String>>>>() {

    override suspend fun invoke(request: Unit): Flow<UiState<List<String>>> {
        return gptRepository.selectAllGptRoles().map {
            it.mapToUiState { it.map { it.role } }
        }
    }
}