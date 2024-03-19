package com.youhajun.domain.usecase.gpt

import com.youhajun.data.repositories.GptRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.gpt.UpdateGptChannelInfoRequestVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateGptChannelInfoUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<UpdateGptChannelInfoRequestVo, Flow<UiState<Unit>>>() {

    override suspend fun invoke(request: UpdateGptChannelInfoRequestVo): Flow<UiState<Unit>> {
        return gptRepository.updateGptChannelInfo(UpdateGptChannelInfoRequestVo.mapModelToDto(request)).map {
            it.mapToUiState {  }
        }
    }
}