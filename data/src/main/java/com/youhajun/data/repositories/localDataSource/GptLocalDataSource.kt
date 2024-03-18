package com.youhajun.data.repositories.localDataSource

import com.youhajun.data.Resource
import com.youhajun.data.dao.GptChannelDao
import com.youhajun.data.dao.GptMessageDao
import com.youhajun.data.dao.GptRoleDao
import com.youhajun.data.models.entity.gpt.GptChannelEntity
import com.youhajun.data.models.entity.gpt.GptMessageEntity
import com.youhajun.data.models.entity.gpt.GptRoleEntity
import com.youhajun.data.network.safeResourceFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GptLocalDataSource @Inject constructor(
    private val gptRoleDao: GptRoleDao,
    private val gptChannelDao: GptChannelDao,
    private val gptMessageDao: GptMessageDao
) {

    fun insertGptRole(role: String) = flow {
        emit(Resource.Success(gptRoleDao.insertRole(GptRoleEntity(role))))
    }.safeResourceFlow()

    fun insertGptMessage(gptMessageEntity: GptMessageEntity) = flow {
        emit(Resource.Success(gptMessageDao.insertMessage(gptMessageEntity)))
    }.safeResourceFlow()

    fun insertGptChannel(gptChannelEntity: GptChannelEntity) = flow {
        emit(Resource.Success(gptChannelDao.insertAndGetChannel(gptChannelEntity)))
    }.safeResourceFlow()

    fun deleteGptRole(role: String) = flow {
        emit(Resource.Success(gptRoleDao.deleteRole(role)))
    }.safeResourceFlow()

    fun deleteGptChannel(idx: Long) = flow {
        emit(Resource.Success(gptChannelDao.deleteChannel(idx)))
    }.safeResourceFlow()

    fun selectAllGptRoles() = gptRoleDao.getAllRoles()
        .map { Resource.Success(it) }
        .safeResourceFlow()

    fun selectAllGptChannels() = gptChannelDao.getAllChannels()
        .map { Resource.Success(it) }
        .safeResourceFlow()

    fun selectGptChannel(idx: Long) = gptChannelDao.getChannelByIdx(idx)
        .map { Resource.Success(it) }
        .safeResourceFlow()

    fun selectLatestChannel() = gptChannelDao.getLatestChannel()
        .map { Resource.Success(it) }
        .safeResourceFlow()

    fun selectAllGptMessages(channelIdx: Long) = gptMessageDao.getAllMessages(channelIdx)
        .map { Resource.Success(it) }
        .safeResourceFlow()
}