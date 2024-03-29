package com.youhajun.data.di

import com.youhajun.data.BuildConfig
import com.youhajun.data.Endpoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Singleton
    @Provides
    @MyTaskWebSocketOkHttpClient
    fun provideRequest(): Request {
        return Request.Builder()
            .url(BuildConfig.WEBSOCKET_BASE_URL + Endpoint.Socket.RTC)
            .build()
    }

    @Singleton
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