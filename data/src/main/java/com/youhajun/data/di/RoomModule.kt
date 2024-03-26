package com.youhajun.data.di

import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.youhajun.data.models.entity.EntityTable
import com.youhajun.data.room.RoomDataBase
import com.youhajun.data.room.RoomInit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @RoomDB
    @Provides
    @Singleton
    fun provideRoomDataBase(
        @ApplicationContext context: Context,
        callback: RoomDatabase.Callback
    ) = Room.databaseBuilder(
        context,
        RoomDataBase::class.java, RoomInit.DB_NAME
    ).addCallback(callback).build()

    @Provides
    @Singleton
    fun provideDatabaseCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                RoomInit.initGptRoles().forEach {
                    db.insert(EntityTable.GPT_ROLE, OnConflictStrategy.IGNORE, it)
                }
            }
        }
    }
}