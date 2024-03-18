package com.youhajun.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.youhajun.data.models.entity.EntityTable
import com.youhajun.data.models.entity.gpt.GptAssistantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GptAssistantDao {

    @Query("SELECT * FROM ${EntityTable.GPT_ASSISTANT} ORDER BY idx DESC LIMIT 1")
    fun getLatestAssistant(): Flow<GptAssistantEntity?>

    @Query("SELECT * FROM ${EntityTable.GPT_ASSISTANT} ORDER BY idx DESC")
    fun getAllAssistants(): Flow<List<GptAssistantEntity>>

    @Query("SELECT * FROM ${EntityTable.GPT_ASSISTANT} WHERE idx = :idx")
    fun getAssistant(idx: Long): Flow<GptAssistantEntity>

    @Query("SELECT * FROM ${EntityTable.GPT_ASSISTANT} WHERE idx = :idx")
    fun getAssistantByIdx(idx: Long): GptAssistantEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAssistant(gptAssistantEntity: GptAssistantEntity): Long

    @Query("DELETE FROM ${EntityTable.GPT_ASSISTANT} WHERE idx = :idx")
    suspend fun deleteAssistant(idx: Long)

    @Transaction
    suspend fun insertAndGetAssistant(gptAssistantEntity: GptAssistantEntity): GptAssistantEntity {
        val idx = insertAssistant(gptAssistantEntity)
        return getAssistantByIdx(idx)
    }
}