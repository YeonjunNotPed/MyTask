package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.model_data.gpt.ChatGptRequest
import com.youhajun.model_data.gpt.ChatGptResponse
import com.youhajun.model_data.ApiResult
import com.youhajun.remote.apiHandle
import com.youhajun.remote.services.ChatGptService
import javax.inject.Inject

class GptRemoteDataSource @Inject constructor(
    private val chatGptService: ChatGptService
) {
    suspend fun postChatGptPrompt(request: ChatGptRequest): ApiResult<ChatGptResponse> =
        apiHandle { chatGptService.postPrompt(request) }
}