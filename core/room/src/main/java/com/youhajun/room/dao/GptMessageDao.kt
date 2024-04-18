package com.youhajun.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youhajun.room.EntityTable
import com.youhajun.room.entity.gpt.GptMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GptMessageDao {
    @Query("SELECT * FROM ${EntityTable.GPT_MESSAGE} WHERE channelIdx = :channelIdx ORDER BY idx DESC")
    fun getMessagesByChannelIdx(channelIdx:Long): Flow<List<GptMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(gptMessageEntity: GptMessageEntity): Long
}