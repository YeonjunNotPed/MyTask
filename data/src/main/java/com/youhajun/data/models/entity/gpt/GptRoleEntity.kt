package com.youhajun.data.models.entity.gpt

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.youhajun.data.models.entity.EntityTable

@Entity(tableName = EntityTable.GPT_ROLE)
data class GptRoleEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_NAME_ROLE) val role: String,
) {
    companion object {
        const val COLUMN_NAME_ROLE = "role"
    }
}