package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.gpt.GptAssistantVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelectAllGptAssistantsUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<Unit, Flow<UiState<List<GptAssistantVo>>>>() {

    override suspend fun invoke(request: Unit): Flow<UiState<List<GptAssistantVo>>> {
        return gptRepository.selectAllGptAssistants().map {
            it.mapToUiState { it.map { GptAssistantVo.mapDtoToModel(it) } }
        }
    }
}