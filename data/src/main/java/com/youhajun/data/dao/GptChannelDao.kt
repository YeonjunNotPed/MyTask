package com.youhajun.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.youhajun.data.models.entity.EntityTable
import com.youhajun.data.models.entity.gpt.GptChannelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface GptChannelDao {

    @Query("SELECT * FROM ${EntityTable.GPT_CHANNEL} ORDER BY idx DESC LIMIT 1")
    fun getLatestChannel(): Flow<GptChannelEntity?>

    @Query("SELECT * FROM ${EntityTable.GPT_CHANNEL} ORDER BY idx DESC")
    fun getAllChannels(): Flow<List<GptChannelEntity>>

    @Query("SELECT * FROM ${EntityTable.GPT_CHANNEL} WHERE idx = :idx")
    fun getChannelByIdx(idx: Long): Flow<GptChannelEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChannel(gptChannelEntity: GptChannelEntity): Long

    @Query("DELETE FROM ${EntityTable.GPT_CHANNEL} WHERE idx = :idx")
    suspend fun deleteChannel(idx: Long)

    @Transaction
    suspend fun insertAndGetChannel(gptChannelEntity: GptChannelEntity): GptChannelEntity {
        val idx = insertChannel(gptChannelEntity)
        return getChannelByIdx(idx).first()
    }
}