package com.youhajun.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.youhajun.room.dao.GptAssistantDao
import com.youhajun.room.dao.GptChannelDao
import com.youhajun.room.dao.GptMessageDao
import com.youhajun.room.dao.GptRoleDao
import com.youhajun.room.dao.RecentSearchDao
import com.youhajun.room.entity.gpt.GptAssistantEntity
import com.youhajun.room.entity.gpt.GptChannelEntity
import com.youhajun.room.entity.gpt.GptMessageEntity
import com.youhajun.room.entity.gpt.GptRoleEntity
import com.youhajun.room.entity.search.RecentSearchEntity

@Database(
    entities = [
        RecentSearchEntity::class,
        GptRoleEntity::class,
        GptChannelEntity::class,
        GptMessageEntity::class,
        GptAssistantEntity::class,
    ],
    version = 1
)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao

    abstract fun gptRoleDao(): GptRoleDao

    abstract fun gptChannelDao(): GptChannelDao

    abstract fun gptMessageDao(): GptMessageDao

    abstract fun gptAssistantDao(): GptAssistantDao
}