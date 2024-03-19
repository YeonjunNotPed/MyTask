package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.gpt.GptChannelVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InsertGptChannelUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<GptChannelVo, Flow<UiState<Long>>>() {

    override suspend fun invoke(request: GptChannelVo): Flow<UiState<Long>> {
        return gptRepository.insertGptChannel(GptChannelVo.mapModelToDto(request)).map {
            it.mapToUiState { it }
        }
    }
}