package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.gpt.GptAssistantVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelectLatestAssistantsUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<Unit, Flow<UiState<GptAssistantVo?>>>() {

    override suspend fun invoke(request: Unit): Flow<UiState<GptAssistantVo?>> {
        return gptRepository.selectLatestAssistant().map {
            it.mapToUiState { it?.let { GptAssistantVo.mapDtoToModel(it) } }
        }
    }
}