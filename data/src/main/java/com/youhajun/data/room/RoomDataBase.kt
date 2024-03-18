package com.youhajun.data.room

import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.youhajun.data.dao.GptAssistantDao
import com.youhajun.data.dao.GptMessageDao
import com.youhajun.data.dao.GptRoleDao
import com.youhajun.data.dao.RecentSearchDao
import com.youhajun.data.models.entity.EntityTable
import com.youhajun.data.models.entity.gpt.GptAssistantEntity
import com.youhajun.data.models.entity.gpt.GptMessageEntity
import com.youhajun.data.models.entity.gpt.GptRoleEntity
import com.youhajun.data.models.entity.search.RecentSearchEntity

@Database(
    entities = [
        RecentSearchEntity::class,
        GptRoleEntity::class,
        GptAssistantEntity::class,
        GptMessageEntity::class
    ],
    version = 1
)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao

    abstract fun gptRoleDao(): GptRoleDao

    abstract fun gptAssistantDao(): GptAssistantDao

    abstract fun gptMessageDao(): GptMessageDao
}