package com.youhajun.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youhajun.data.models.entity.EntityTable
import com.youhajun.data.models.entity.gpt.GptMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GptMessageDao {
    @Query("SELECT * FROM ${EntityTable.GPT_MESSAGE} WHERE assistantIdx = :assistantIdx ORDER BY idx DESC")
    fun getAllMessages(assistantIdx:Long): Flow<List<GptMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(gptMessageEntity: GptMessageEntity)
}