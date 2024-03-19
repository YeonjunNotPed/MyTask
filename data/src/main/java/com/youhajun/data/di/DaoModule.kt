package com.youhajun.data.di

import com.youhajun.data.room.RoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideUserInfoDao(@RoomDB db: RoomDataBase) = db.recentSearchDao()

    @Provides
    fun provideGptRoleDao(@RoomDB db: RoomDataBase) = db.gptRoleDao()

    @Provides
    fun provideGptChannelDao(@RoomDB db: RoomDataBase) = db.gptChannelDao()

    @Provides
    fun provideGptMessageDao(@RoomDB db: RoomDataBase) = db.gptMessageDao()

    @Provides
    fun provideGptAssistantDao(@RoomDB db: RoomDataBase) = db.gptAssistantDao()
}