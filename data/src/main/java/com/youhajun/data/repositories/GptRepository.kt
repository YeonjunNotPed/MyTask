package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.models.dto.gpt.ChatGptRequest
import com.youhajun.data.models.dto.gpt.ChatGptResponse
import com.youhajun.data.models.entity.gpt.GptAssistantEntity
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

    fun insertGptAssistant(assistantEntity: GptAssistantEntity): Flow<Resource<GptAssistantEntity>> {
        return gptLocalDataSource.insertGptAssistant(assistantEntity).onStart { emit(Resource.Loading()) }
    }

    fun deleteGptRole(role: String): Flow<Resource<Unit>> {
        return gptLocalDataSource.deleteGptRole(role)
    }

    fun deleteGptAssistant(idx: Long): Flow<Resource<Unit>> {
        return gptLocalDataSource.deleteGptAssistant(idx)
    }

    fun selectAllGptRoles(): Flow<Resource<List<GptRoleEntity>>> {
        return gptLocalDataSource.selectAllGptRoles()
    }

    fun selectAllGptAssistants(): Flow<Resource<List<GptAssistantEntity>>> {
        return gptLocalDataSource.selectAllGptAssistants()
    }

    fun selectGptAssistant(idx:Long): Flow<Resource<GptAssistantEntity>> {
        return gptLocalDataSource.selectGptAssistant(idx)
    }

    fun selectLatestAssistant(): Flow<Resource<GptAssistantEntity?>> {
        return gptLocalDataSource.selectLatestAssistant()
    }
}

