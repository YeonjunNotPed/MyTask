package com.youhajun.data.models.entity.gpt

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.youhajun.data.models.entity.EntityTable

@Entity(
    tableName = EntityTable.GPT_ASSISTANT,
    foreignKeys = [ForeignKey(
        entity = GptChannelEntity::class,
        parentColumns = arrayOf(GptChannelEntity.COLUMN_NAME_IDX),
        childColumns = arrayOf(GptAssistantEntity.COLUMN_NAME_CHANNEL_IDX),
        onDelete = ForeignKey.CASCADE
    )]
)
data class GptAssistantEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_NAME_IDX)
    val idx: Long = 0,
    @ColumnInfo(name = COLUMN_NAME_CHANNEL_IDX)
    val channelIdx: Long,
    @ColumnInfo(name = COLUMN_NAME_GPT_ASSISTANT_MESSAGE)
    val assistantMessage: String,
    @ColumnInfo(name = COLUMN_NAME_CREATED_AT_UNIX_TIME)
    val createdAtUnixTimeStamp: Long,
) {

    companion object {
        const val COLUMN_NAME_IDX = "idx"
        const val COLUMN_NAME_CHANNEL_IDX = "channelIdx"
        const val COLUMN_NAME_GPT_ASSISTANT_MESSAGE = "assistantMessage"
        const val COLUMN_NAME_CREATED_AT_UNIX_TIME = "createdAt"
    }
}