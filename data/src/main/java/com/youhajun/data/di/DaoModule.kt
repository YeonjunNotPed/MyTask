package com.youhajun.data.di

import com.youhajun.data.RoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideUserInfoDao(@RoomDB db: RoomDataBase) = db.recentSearchDao()
}