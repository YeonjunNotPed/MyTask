package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.models.dto.gpt.ChatGptRequest
import com.youhajun.data.models.dto.gpt.ChatGptResponse
import com.youhajun.data.repositories.base.BaseRepository
import com.youhajun.data.repositories.remoteDataSource.GptDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GptRepository @Inject constructor(
    private val gptDataSource: GptDataSource
) : BaseRepository() {

    fun postChatGptPrompt(request: ChatGptRequest): Flow<Resource<ChatGptResponse>> =
        gptDataSource.postChatGptPrompt(request).map { apiConverter(it) }
}

