package com.youhajun.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youhajun.room.EntityTable
import com.youhajun.room.entity.gpt.GptChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GptChannelDao {

    @Query("SELECT * FROM ${EntityTable.GPT_CHANNEL} ORDER BY idx DESC LIMIT 1")
    fun getLatestChannel(): Flow<GptChannelEntity?>

    @Query("SELECT * FROM ${EntityTable.GPT_CHANNEL} ORDER BY idx DESC")
    fun getAllChannels(): Flow<List<GptChannelEntity>>

    @Query("SELECT * FROM ${EntityTable.GPT_CHANNEL} WHERE idx = :idx")
    fun getChannelByIdx(idx: Long): Flow<GptChannelEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChannel(gptChannelEntity: GptChannelEntity): Long

    @Query("DELETE FROM ${EntityTable.GPT_CHANNEL} WHERE idx = :idx")
    suspend fun deleteChannel(idx: Long)

    @Query("UPDATE ${EntityTable.GPT_CHANNEL} SET roleOfAi = :roleOfAi, lastQuestion = :lastQuestion, gptType = :gptType WHERE idx = :idx")
    suspend fun updateGptChannelInfo(idx: Long, roleOfAi:String?, lastQuestion: String, gptType: String)

}