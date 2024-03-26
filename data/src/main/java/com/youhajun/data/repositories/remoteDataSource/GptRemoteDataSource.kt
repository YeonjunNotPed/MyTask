package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.data.models.dto.gpt.ChatGptRequest
import com.youhajun.data.models.dto.gpt.ChatGptResponse
import com.youhajun.data.network.safeResponseFlow
import com.youhajun.data.services.ChatGptService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class GptRemoteDataSource @Inject constructor(
    private val chatGptService: ChatGptService
) {
    fun postChatGptPrompt(request:ChatGptRequest): Flow<Response<ChatGptResponse>> = flow {
        emit(chatGptService.postPrompt(request))
    }.safeResponseFlow()
}