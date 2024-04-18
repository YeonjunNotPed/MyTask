package com.youhajun.room.entity.gpt

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.youhajun.model_data.gpt.GptMessageEntityDto
import com.youhajun.room.EntityTable
import com.youhajun.room.ToDto
import com.youhajun.room.ToEntity

@Entity(
    tableName = EntityTable.GPT_MESSAGE,
    foreignKeys = [ForeignKey(
        entity = GptChannelEntity::class,
        parentColumns = arrayOf(GptChannelEntity.COLUMN_NAME_IDX),
        childColumns = arrayOf(GptMessageEntity.COLUMN_NAME_CHANNEL_IDX),
        onDelete = ForeignKey.CASCADE
    )]
)
data class GptMessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_NAME_IDX)
    val idx: Long = 0,
    @ColumnInfo(name = COLUMN_NAME_CHANNEL_IDX)
    val channelIdx: Long,
    @ColumnInfo(name = COLUMN_NAME_GPT_MESSAGE_TYPE)
    val gptMessageType: String,
    @ColumnInfo(name = COLUMN_NAME_GPT_MESSAGE)
    val message: String,
    @ColumnInfo(name = COLUMN_NAME_CREATED_AT_UNIX_TIME)
    val createdAtUnixTimeStamp: Long,
) {

    companion object : ToDto<GptMessageEntity, GptMessageEntityDto>, ToEntity<GptMessageEntity, GptMessageEntityDto> {
        const val COLUMN_NAME_IDX = "idx"
        const val COLUMN_NAME_CHANNEL_IDX = "channelIdx"
        const val COLUMN_NAME_GPT_MESSAGE_TYPE = "gptMessageType"
        const val COLUMN_NAME_GPT_MESSAGE = "message"
        const val COLUMN_NAME_CREATED_AT_UNIX_TIME = "createdAt"

        override fun GptMessageEntity.toDto(): GptMessageEntityDto = GptMessageEntityDto(
            idx, channelIdx, gptMessageType, message, createdAtUnixTimeStamp
        )

        override fun GptMessageEntityDto.toEntity(): GptMessageEntity = GptMessageEntity(
            idx, channelIdx, gptMessageType, message, createdAtUnixTimeStamp
        )
    }
}