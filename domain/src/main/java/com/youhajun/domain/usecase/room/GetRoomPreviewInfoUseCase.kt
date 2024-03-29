package com.youhajun.domain.usecase.room

import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.room.RoomPreviewInfoVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRoomPreviewInfoUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : UseCase<Unit, Flow<UiState<RoomPreviewInfoVo>>>() {
    override suspend fun invoke(request: Unit): Flow<UiState<RoomPreviewInfoVo>> {
        return roomRepository.getRoomPreviewInfo().map {
            it.mapToUiState { RoomPreviewInfoVo.mapDtoToModel(it) }
        }
    }
}