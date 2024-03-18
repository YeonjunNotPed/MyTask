package com.youhajun.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.youhajun.data.dao.GptChannelDao
import com.youhajun.data.dao.GptMessageDao
import com.youhajun.data.dao.GptRoleDao
import com.youhajun.data.dao.RecentSearchDao
import com.youhajun.data.models.entity.gpt.GptChannelEntity
import com.youhajun.data.models.entity.gpt.GptMessageEntity
import com.youhajun.data.models.entity.gpt.GptRoleEntity
import com.youhajun.data.models.entity.search.RecentSearchEntity

@Database(
    entities = [
        RecentSearchEntity::class,
        GptRoleEntity::class,
        GptChannelEntity::class,
        GptMessageEntity::class
    ],
    version = 1
)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao

    abstract fun gptRoleDao(): GptRoleDao

    abstract fun gptChannelDao(): GptChannelDao

    abstract fun gptMessageDao(): GptMessageDao
}