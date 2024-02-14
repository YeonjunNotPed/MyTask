package com.youhajun.data.di

import android.content.Context
import androidx.room.Room
import com.youhajun.data.RoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DB_NAME = "database-my-task"

    @Provides
    fun provideRoomDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RoomDataBase::class.java, DB_NAME
    ).build()
}