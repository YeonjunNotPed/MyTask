package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.gpt.GptChannelVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelectLatestChannelsUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<Unit, Flow<UiState<GptChannelVo?>>>() {

    override suspend fun invoke(request: Unit): Flow<UiState<GptChannelVo?>> {
        return gptRepository.selectLatestChannel().map {
            it.mapToUiState { it?.let { GptChannelVo.mapDtoToModel(it) } }
        }
    }
}