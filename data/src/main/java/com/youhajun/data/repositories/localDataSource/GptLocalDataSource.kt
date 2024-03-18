package com.youhajun.data.repositories.localDataSource

import com.youhajun.data.Resource
import com.youhajun.data.dao.GptAssistantDao
import com.youhajun.data.dao.GptMessageDao
import com.youhajun.data.dao.GptRoleDao
import com.youhajun.data.models.entity.gpt.GptAssistantEntity
import com.youhajun.data.models.entity.gpt.GptMessageEntity
import com.youhajun.data.models.entity.gpt.GptRoleEntity
import com.youhajun.data.network.safeResourceFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GptLocalDataSource @Inject constructor(
    private val gptRoleDao: GptRoleDao,
    private val gptAssistantDao: GptAssistantDao,
    private val gptMessageDao: GptMessageDao
) {

    fun insertGptRole(role: String) = flow {
        emit(Resource.Success(gptRoleDao.insertRole(GptRoleEntity(role))))
    }.safeResourceFlow()

    fun insertGptMessage(gptMessageEntity: GptMessageEntity) = flow {
        emit(Resource.Success(gptMessageDao.insertMessage(gptMessageEntity)))
    }.safeResourceFlow()

    fun insertGptAssistant(gptAssistantEntity: GptAssistantEntity) = flow {
        emit(Resource.Success(gptAssistantDao.insertAndGetAssistant(gptAssistantEntity)))
    }.safeResourceFlow()

    fun deleteGptRole(role: String) = flow {
        emit(Resource.Success(gptRoleDao.deleteRole(role)))
    }.safeResourceFlow()

    fun deleteGptAssistant(idx: Long) = flow {
        emit(Resource.Success(gptAssistantDao.deleteAssistant(idx)))
    }.safeResourceFlow()

    fun selectAllGptRoles() = gptRoleDao.getAllRoles()
        .map { Resource.Success(it) }
        .safeResourceFlow()

    fun selectAllGptAssistants() = gptAssistantDao.getAllAssistants()
        .map { Resource.Success(it) }
        .safeResourceFlow()

    fun selectGptAssistant(idx: Long) = gptAssistantDao.getAssistant(idx)
        .map { Resource.Success(it) }
        .safeResourceFlow()

    fun selectLatestAssistant() = gptAssistantDao.getLatestAssistant()
        .map { Resource.Success(it) }
        .safeResourceFlow()

    fun selectAllGptMessages(assistantIdx: Long) = gptMessageDao.getAllMessages(assistantIdx)
        .map { Resource.Success(it) }
        .safeResourceFlow()
}