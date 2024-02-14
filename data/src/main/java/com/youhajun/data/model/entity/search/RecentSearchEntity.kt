package com.youhajun.data.model.entity.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.youhajun.data.model.entity.EntityTable

@Entity(tableName = EntityTable.RECENT_SEARCH)
data class RecentSearchEntity(
    @PrimaryKey
    @ColumnInfo(name = "search") val search: String,
    @ColumnInfo(name = "saveTime") val saveTime: Long
)