package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.models.dto.gpt.ChatGptRequest
import com.youhajun.data.models.dto.gpt.ChatGptResponse
import com.youhajun.data.models.entity.gpt.GptChannelEntity
import com.youhajun.data.models.entity.gpt.GptRoleEntity
import com.youhajun.data.repositories.base.BaseRepository
import com.youhajun.data.repositories.localDataSource.GptLocalDataSource
import com.youhajun.data.repositories.remoteDataSource.GptRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GptRepository @Inject constructor(
    private val gptRemoteDataSource: GptRemoteDataSource,
    private val gptLocalDataSource: GptLocalDataSource
) : BaseRepository() {

    fun postChatGptPrompt(request: ChatGptRequest): Flow<Resource<ChatGptResponse>> =
        gptRemoteDataSource.postChatGptPrompt(request).map { apiConverter(it) }

    fun insertGptRole(role: String): Flow<Resource<Unit>> {
        return gptLocalDataSource.insertGptRole(role)
    }

    fun insertGptChannel(channelEntity: GptChannelEntity): Flow<Resource<GptChannelEntity>> {
        return gptLocalDataSource.insertGptChannel(channelEntity).onStart { emit(Resource.Loading()) }
    }

    fun deleteGptRole(role: String): Flow<Resource<Unit>> {
        return gptLocalDataSource.deleteGptRole(role)
    }

    fun deleteGptChannel(idx: Long): Flow<Resource<Unit>> {
        return gptLocalDataSource.deleteGptChannel(idx)
    }

    fun selectAllGptRoles(): Flow<Resource<List<GptRoleEntity>>> {
        return gptLocalDataSource.selectAllGptRoles()
    }

    fun selectAllGptChannels(): Flow<Resource<List<GptChannelEntity>>> {
        return gptLocalDataSource.selectAllGptChannels()
    }

    fun selectGptChannel(idx:Long): Flow<Resource<GptChannelEntity>> {
        return gptLocalDataSource.selectGptChannel(idx)
    }

    fun selectLatestChannel(): Flow<Resource<GptChannelEntity?>> {
        return gptLocalDataSource.selectLatestChannel()
    }
}

