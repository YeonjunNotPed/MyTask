package com.youhajun.data.models.entity.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.youhajun.data.models.entity.EntityTable

@Entity(tableName = EntityTable.RECENT_SEARCH)
data class RecentSearchEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_NAME_SEARCH) val search: String,
    @ColumnInfo(name = COLUMN_NAME_SAVE_TIME) val saveTime: Long
) {
    companion object {
        const val COLUMN_NAME_SEARCH = "search"
        const val COLUMN_NAME_SAVE_TIME = "saveTime"
    }
}