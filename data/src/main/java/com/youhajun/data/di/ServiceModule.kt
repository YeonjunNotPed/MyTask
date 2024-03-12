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

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideChatGptService(@ChatGptRetrofit retrofit: Retrofit): ChatGptService {
        return retrofit.create(ChatGptService::class.java)
    }


    @Provides
    fun provideAuthService(@MyTaskRetrofit retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    fun provideStoreService(@MyTaskRetrofit retrofit: Retrofit): StoreService {
        return retrofit.create(StoreService::class.java)
    }

    @Provides
    fun provideSignService(@MyTaskRetrofit retrofit: Retrofit): SignService {
        return retrofit.create(SignService::class.java)
    }

    @Provides
    fun provideRoomService(@MyTaskRetrofit retrofit: Retrofit): RoomService {
        return retrofit.create(RoomService::class.java)
    }
}