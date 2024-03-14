package com.youhajun.data.network

import com.youhajun.data.BuildConfig
import com.youhajun.data.models.enums.GptAiType
import okhttp3.Interceptor
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GptInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val token = when(chain.request().getGptAiType()) {
            GptAiType.CHAT_GPT -> BuildConfig.CHAT_GPT_API_KEY
            GptAiType.GEMINI -> BuildConfig.GEMINI_API_KEY
        }

        val request = chain
            .request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
    private fun Request.getGptAiType() = GptAiType.fromUrl(url.toString())
}