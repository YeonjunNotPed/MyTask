package com.youhajun.remote

import com.youhajun.core.remote.BuildConfig
import com.youhajun.model_data.types.GptAiTypeDto
import okhttp3.Interceptor
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GptInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val token = when(chain.request().getGptAiType()) {
            GptAiTypeDto.CHAT_GPT -> BuildConfig.CHAT_GPT_API_KEY
            GptAiTypeDto.GEMINI -> BuildConfig.GEMINI_API_KEY
        }

        val request = chain
            .request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
    private fun Request.getGptAiType():GptAiTypeDto {
        val url = this.url.toString()
        return when {
            url.startsWith(Endpoint.ChatGpt.BASE_URL) -> GptAiTypeDto.CHAT_GPT
            url.startsWith(Endpoint.Gemini.BASE_URL) -> GptAiTypeDto.GEMINI
            else -> GptAiTypeDto.CHAT_GPT
        }
    }
}