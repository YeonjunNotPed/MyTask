package com.youhajun.data.services

import com.youhajun.data.Endpoint
import com.youhajun.data.model.dto.gpt.ChatGptRequest
import com.youhajun.data.model.dto.gpt.ChatGptResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGptService {
    @POST(Endpoint.ChatGpt.CHAT_COMPLETIONS)
    suspend fun getPrompt(@Body request: ChatGptRequest): Response<ChatGptResponse>
}