package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeleteGptChannelUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<Long, Flow<UiState<Unit>>>() {

    override suspend fun invoke(request: Long): Flow<UiState<Unit>> {
        return gptRepository.deleteGptChannel(request).map {
            it.mapToUiState {  }
        }
    }
}