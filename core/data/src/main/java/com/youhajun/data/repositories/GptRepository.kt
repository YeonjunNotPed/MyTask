package com.youhajun.data.repositories

import com.youhajun.data.repositories.localDataSource.GptLocalDataSource
import com.youhajun.data.repositories.remoteDataSource.GptRemoteDataSource
import com.youhajun.model_data.gpt.ChatGptRequest
import com.youhajun.model_data.gpt.ChatGptResponse
import com.youhajun.model_data.gpt.GptAssistantEntityDto
import com.youhajun.model_data.gpt.GptChannelEntityDto
import com.youhajun.model_data.gpt.GptMessageEntityDto
import com.youhajun.model_data.gpt.GptRoleEntityDto
import com.youhajun.model_data.gpt.UpdateGptChannelInfoRequest
import com.youhajun.model_data.ApiResult
import com.youhajun.room.entity.gpt.GptAssistantEntity.Companion.toDto
import com.youhajun.room.entity.gpt.GptAssistantEntity.Companion.toEntity
import com.youhajun.room.entity.gpt.GptChannelEntity.Companion.toDto
import com.youhajun.room.entity.gpt.GptChannelEntity.Companion.toEntity
import com.youhajun.room.entity.gpt.GptMessageEntity.Companion.toDto
import com.youhajun.room.entity.gpt.GptMessageEntity.Companion.toEntity
import com.youhajun.room.entity.gpt.GptRoleEntity.Companion.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GptRepository @Inject constructor(
    private val gptRemoteDataSource: GptRemoteDataSource,
    private val gptLocalDataSource: GptLocalDataSource
) {

    suspend fun postChatGptPrompt(request: ChatGptRequest): ApiResult<ChatGptResponse> =
        gptRemoteDataSource.postChatGptPrompt(request)

    suspend fun insertGptRole(role: String): Unit =
        gptLocalDataSource.insertGptRole(role)

    suspend fun insertGptChannel(dto: GptChannelEntityDto): Long =
        gptLocalDataSource.insertGptChannel(dto.toEntity())

    suspend fun insertGptAssistant(dto: GptAssistantEntityDto): Unit =
        gptLocalDataSource.insertGptAssistant(dto.toEntity())

    suspend fun insertGptMessage(dto: GptMessageEntityDto): Long =
        gptLocalDataSource.insertGptMessage(dto.toEntity())

    suspend fun deleteGptRole(role: String): Unit =
        gptLocalDataSource.deleteGptRole(role)

    suspend fun deleteGptChannel(idx: Long): Unit =
        gptLocalDataSource.deleteGptChannel(idx)

    suspend fun updateGptChannelInfo(request: UpdateGptChannelInfoRequest) =
        gptLocalDataSource.updateGptChannelInfo(request)

    fun selectAllGptRoles(): Flow<List<GptRoleEntityDto>> =
        gptLocalDataSource.selectAllGptRoles().map { it.map { it.toDto() } }

    fun selectAllGptChannels(): Flow<List<GptChannelEntityDto>> =
        gptLocalDataSource.selectAllGptChannels().map { it.map { it.toDto() } }

    fun selectLatestChannel(): Flow<GptChannelEntityDto> =
        gptLocalDataSource.selectLatestChannel().map { it.toDto() }

    fun selectGptChannel(idx: Long): Flow<GptChannelEntityDto> =
        gptLocalDataSource.selectGptChannel(idx).map { it.toDto() }

    fun selectAllGptMessages(channelIdx: Long): Flow<List<GptMessageEntityDto>> =
        gptLocalDataSource.selectAllGptMessages(channelIdx).map { it.map { it.toDto() } }

    fun selectAllGptAssistants(channelIdx: Long): Flow<List<GptAssistantEntityDto>> =
        gptLocalDataSource.selectAllGptAssistants(channelIdx).map { it.map { it.toDto() } }
}