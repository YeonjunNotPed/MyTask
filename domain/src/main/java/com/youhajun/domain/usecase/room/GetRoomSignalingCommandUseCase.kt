package com.youhajun.domain.usecase.room

import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRoomSignalingCommandUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : UseCase<Unit, Flow<Pair<SignalingType, String>>>() {
    override suspend fun invoke(request: Unit): Flow<Pair<SignalingType, String>> {
        return roomRepository.signalingCommandFlow.map {
            SignalingType.typeOf(it.first.type) to it.second
        }
    }
}