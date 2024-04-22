package com.youhajun.data.repositories.localDataSource

import com.youhajun.model_data.gpt.UpdateGptChannelInfoRequest
import com.youhajun.room.dao.GptAssistantDao
import com.youhajun.room.dao.GptChannelDao
import com.youhajun.room.dao.GptMessageDao
import com.youhajun.room.dao.GptRoleDao
import com.youhajun.room.entity.gpt.GptAssistantEntity
import com.youhajun.room.entity.gpt.GptChannelEntity
import com.youhajun.room.entity.gpt.GptMessageEntity
import com.youhajun.room.entity.gpt.GptRoleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class GptLocalDataSource @Inject constructor(
    private val gptRoleDao: GptRoleDao,
    private val gptChannelDao: GptChannelDao,
    private val gptMessageDao: GptMessageDao,
    private val gptAssistantDao: GptAssistantDao
) {

    suspend fun insertGptRole(role: String): Unit = gptRoleDao.insertRole(GptRoleEntity(role))

    suspend fun insertGptMessage(gptMessageEntity: GptMessageEntity): Long =
        gptMessageDao.insertMessage(gptMessageEntity)

    suspend fun insertGptChannel(gptChannelEntity: GptChannelEntity): Long =
        gptChannelDao.insertChannel(gptChannelEntity)

    suspend fun insertGptAssistant(gptAssistantEntity: GptAssistantEntity): Unit =
        gptAssistantDao.insertAssistantAndDeleteOldest(gptAssistantEntity)

    suspend fun deleteGptRole(role: String): Unit = gptRoleDao.deleteRole(role)

    suspend fun deleteGptChannel(idx: Long): Unit = gptChannelDao.deleteChannel(idx)

    suspend fun updateGptChannelInfo(request: UpdateGptChannelInfoRequest): Unit = with(request) {
        gptChannelDao.updateGptChannelInfo(idx, roleOfAi, lastQuestion, gptType)
    }

    fun selectAllGptRoles(): Flow<List<GptRoleEntity>> = gptRoleDao.getAllRoles()
    fun selectAllGptChannels(): Flow<List<GptChannelEntity>> = gptChannelDao.getAllChannels()

    fun selectLatestChannel(): Flow<GptChannelEntity> =
        gptChannelDao.getLatestChannel().filterNotNull()

    fun selectGptChannel(idx: Long): Flow<GptChannelEntity> =
        gptChannelDao.getChannelByIdx(idx).filterNotNull()

    fun selectAllGptMessages(channelIdx: Long): Flow<List<GptMessageEntity>> =
        gptMessageDao.getMessagesByChannelIdx(channelIdx)

    fun selectAllGptAssistants(channelIdx: Long): Flow<List<GptAssistantEntity>> =
        gptAssistantDao.getAssistantsByChannelIdx(channelIdx)
}