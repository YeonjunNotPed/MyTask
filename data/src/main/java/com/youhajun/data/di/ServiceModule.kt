package com.youhajun.data.di

import com.youhajun.data.services.AuthService
import com.youhajun.data.services.ChatGptService
import com.youhajun.data.services.RoomService
import com.youhajun.data.services.SignService
import com.youhajun.data.services.StoreService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideChatGptService(@ChatGptRetrofit retrofit: Retrofit): ChatGptService {
        return retrofit.create(ChatGptService::class.java)
    }


    @Provides
    @Singleton
    fun provideAuthService(@MyTaskRetrofit retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreService(@MyTaskRetrofit retrofit: Retrofit): StoreService {
        return retrofit.create(StoreService::class.java)
    }

    @Provides
    @Singleton
    fun provideSignService(@MyTaskRetrofit retrofit: Retrofit): SignService {
        return retrofit.create(SignService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomService(@MyTaskRetrofit retrofit: Retrofit): RoomService {
        return retrofit.create(RoomService::class.java)
    }
}