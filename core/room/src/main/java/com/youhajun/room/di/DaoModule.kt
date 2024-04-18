package com.youhajun.room.di

import com.youhajun.room.RoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideUserInfoDao(db: RoomDataBase) = db.recentSearchDao()

    @Provides
    fun provideGptRoleDao(db: RoomDataBase) = db.gptRoleDao()

    @Provides
    fun provideGptChannelDao(db: RoomDataBase) = db.gptChannelDao()

    @Provides
    fun provideGptMessageDao(db: RoomDataBase) = db.gptMessageDao()

    @Provides
    fun provideGptAssistantDao(db: RoomDataBase) = db.gptAssistantDao()
}