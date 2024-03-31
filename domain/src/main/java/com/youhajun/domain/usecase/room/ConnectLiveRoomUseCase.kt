package com.youhajun.domain.usecase.room

import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.usecase.NonSuspendUseCase
import com.youhajun.domain.usecase.UseCase
import javax.inject.Inject

class ConnectLiveRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : NonSuspendUseCase<Unit, Unit>() {
    override fun invoke(request: Unit) {
        roomRepository.connectLiveRoom()
    }
}