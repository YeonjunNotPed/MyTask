package com.youhajun.domain.usecase.room

import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.usecase.UseCase
import javax.inject.Inject

class SendLiveRoomMsgUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : UseCase<String, Unit>() {
    override suspend fun invoke(request: String) {
        roomRepository.sendMessage(request)
    }
}