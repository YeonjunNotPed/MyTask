package com.youhajun.domain.usecase.room

import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.sealeds.WebSocketState
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRoomSocketStateUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : UseCase<Unit, Flow<UiState<WebSocketState>>>() {
    override suspend fun invoke(request: Unit): Flow<UiState<WebSocketState>> {
        return roomRepository.socketFlow.map {
            it.mapToUiState { WebSocketState.mapDtoToModel(it) }
        }
    }
}