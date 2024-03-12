package com.youhajun.domain.usecase.room

import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.usecase.UseCase
import javax.inject.Inject

class DisposeLiveRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : UseCase<Unit, Unit>() {
    override suspend fun invoke(request: Unit) {
        roomRepository.dispose()
    }
}