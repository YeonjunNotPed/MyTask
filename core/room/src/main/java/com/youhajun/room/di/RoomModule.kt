package com.youhajun.room.di

import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.youhajun.room.EntityTable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(
        @ApplicationContext context: Context,
        callback: RoomDatabase.Callback
    ) = Room.databaseBuilder(
        context,
        com.youhajun.room.RoomDataBase::class.java, com.youhajun.room.RoomInit.DB_NAME
    ).addCallback(callback).build()

    @Provides
    @Singleton
    fun provideDatabaseCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                com.youhajun.room.RoomInit.initGptRoles().forEach {
                    db.insert(EntityTable.GPT_ROLE, OnConflictStrategy.IGNORE, it)
                }
            }
        }
    }
}