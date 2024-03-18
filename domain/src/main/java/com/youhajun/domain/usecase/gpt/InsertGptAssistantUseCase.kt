package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.gpt.ChatGptRequestVo
import com.youhajun.domain.models.vo.gpt.ChatGptResponseVo
import com.youhajun.domain.models.vo.gpt.GptAssistantVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InsertGptAssistantUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<GptAssistantVo, Flow<UiState<GptAssistantVo>>>() {

    override suspend fun invoke(request: GptAssistantVo): Flow<UiState<GptAssistantVo>> {
        return gptRepository.insertGptAssistant(GptAssistantVo.mapModelToDto(request)).map {
            it.mapToUiState { GptAssistantVo.mapDtoToModel(it) }
        }
    }
}