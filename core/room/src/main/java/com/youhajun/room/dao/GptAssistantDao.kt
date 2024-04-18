package com.youhajun.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.youhajun.room.EntityTable
import com.youhajun.room.entity.gpt.GptAssistantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GptAssistantDao {
    companion object {
        private const val ASSISTANT_SELECT_LIMIT = 5
        private const val ASSISTANT_INSERT_LIMIT = 100
    }

    @Query("SELECT * FROM ${EntityTable.GPT_ASSISTANT} WHERE channelIdx = :channelIdx ORDER BY idx DESC LIMIT $ASSISTANT_SELECT_LIMIT")
    fun getAssistantsByChannelIdx(channelIdx:Long): Flow<List<GptAssistantEntity>>

    @Transaction
    suspend fun insertAssistantAndDeleteOldest(assistantEntity: GptAssistantEntity) {
        val count = getAssistantCountByChannelIdx(assistantEntity.channelIdx)
        if (count >= ASSISTANT_INSERT_LIMIT) deleteOldestRowByChannelIdx(assistantEntity.channelIdx)
        insertAssistant(assistantEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssistant(assistantEntity: GptAssistantEntity): Long

    @Query("SELECT COUNT(*) FROM ${EntityTable.GPT_ASSISTANT} WHERE channelIdx = :channelIdx")
    suspend fun getAssistantCountByChannelIdx(channelIdx:Long): Int

    @Query("DELETE FROM ${EntityTable.GPT_ASSISTANT} WHERE idx = (SELECT idx FROM ${EntityTable.GPT_ASSISTANT} WHERE channelIdx = :channelIdx ORDER BY idx ASC LIMIT 1)")
    suspend fun deleteOldestRowByChannelIdx(channelIdx:Long)
}