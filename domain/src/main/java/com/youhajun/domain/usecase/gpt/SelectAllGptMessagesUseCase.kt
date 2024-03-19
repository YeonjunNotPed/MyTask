package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.gpt.GptMessageVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelectAllGptMessagesUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<Long, Flow<UiState<List<GptMessageVo>>>>() {

    override suspend fun invoke(request: Long): Flow<UiState<List<GptMessageVo>>> {
        return gptRepository.selectAllGptMessages(request).map {
            it.mapToUiState { it.map { GptMessageVo.mapDtoToModel(it) } }
        }
    }
}