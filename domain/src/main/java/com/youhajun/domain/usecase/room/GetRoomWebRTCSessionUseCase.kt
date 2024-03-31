package com.youhajun.domain.usecase.room

import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.models.enums.WebRTCSessionType
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRoomWebRTCSessionUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : UseCase<Unit, Flow<WebRTCSessionType>>() {
    override suspend fun invoke(request: Unit): Flow<WebRTCSessionType> {
        return roomRepository.sessionStateFlow.map {
            WebRTCSessionType.sessionStateOf(it)
        }
    }
}