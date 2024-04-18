package com.youhajun.remote.services

import com.youhajun.model_data.gpt.ChatGptRequest
import com.youhajun.model_data.gpt.ChatGptResponse
import com.youhajun.remote.Endpoint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGptService {
    @POST(Endpoint.ChatGpt.CHAT_COMPLETIONS)
    suspend fun postPrompt(@Body request: ChatGptRequest): Response<ChatGptResponse>
}