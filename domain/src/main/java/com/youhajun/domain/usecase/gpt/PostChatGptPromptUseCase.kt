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

class PostChatGptPromptUseCase @Inject constructor(
    private val gptRepository: GptRepository
) : UseCase<ChatGptRequestVo, Flow<UiState<ChatGptResponseVo>>>() {

    override suspend fun invoke(request: ChatGptRequestVo): Flow<UiState<ChatGptResponseVo>> {
        return gptRepository.postChatGptPrompt(ChatGptRequestVo.mapModelToDto(request)).map {
            it.mapToUiState { ChatGptResponseVo.mapDtoToModel(it) }
        }
    }
}