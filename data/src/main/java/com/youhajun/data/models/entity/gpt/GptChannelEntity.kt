package com.youhajun.data.models.entity.gpt

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.youhajun.data.models.entity.EntityTable

@Entity(tableName = EntityTable.GPT_CHANNEL)
data class GptChannelEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_NAME_IDX)
    val idx:Long = 0,
    @ColumnInfo(name = COLUMN_NAME_GPT_TYPE)
    val gptType: String,
    @ColumnInfo(name = COLUMN_NAME_ROLE_OF_AI)
    val roleOfAi: String? = null,
    @ColumnInfo(name = COLUMN_NAME_LAST_QUESTION)
    val lastQuestion: String? = null,
    @ColumnInfo(name = COLUMN_NAME_CREATED_AT_UNIX_TIME)
    val createdAtUnixTimeStamp: Long,
) {

    companion object {
        const val COLUMN_NAME_IDX = "idx"
        const val COLUMN_NAME_GPT_TYPE = "gptType"
        const val COLUMN_NAME_ROLE_OF_AI = "roleOfAi"
        const val COLUMN_NAME_LAST_QUESTION = "lastQuestion"
        const val COLUMN_NAME_CREATED_AT_UNIX_TIME = "createdAt"
    }
}