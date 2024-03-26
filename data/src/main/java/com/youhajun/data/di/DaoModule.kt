package com.youhajun.data.di

import com.youhajun.data.room.RoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideUserInfoDao(@RoomDB db: RoomDataBase) = db.recentSearchDao()

    @Provides
    @Singleton
    fun provideGptRoleDao(@RoomDB db: RoomDataBase) = db.gptRoleDao()

    @Provides
    @Singleton
    fun provideGptChannelDao(@RoomDB db: RoomDataBase) = db.gptChannelDao()

    @Provides
    @Singleton
    fun provideGptMessageDao(@RoomDB db: RoomDataBase) = db.gptMessageDao()

    @Provides
    @Singleton
    fun provideGptAssistantDao(@RoomDB db: RoomDataBase) = db.gptAssistantDao()
}