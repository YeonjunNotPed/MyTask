package com.youhajun.data.di

import com.youhajun.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @MyTaskWebSocketOkHttpClient
    fun provideRequest(): Request {
        return Request.Builder()
            .url(BuildConfig.WEBSOCKET_BASE_URL)
            .build()
    }

    @Provides
    @MyTaskWebSocketOkHttpClient
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .build()
    }
}