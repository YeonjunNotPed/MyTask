package com.youhajun.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.youhajun.data.dao.RecentSearchDao
import com.youhajun.data.models.entity.search.RecentSearchEntity

@Database(entities = [RecentSearchEntity::class], version = 1)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}