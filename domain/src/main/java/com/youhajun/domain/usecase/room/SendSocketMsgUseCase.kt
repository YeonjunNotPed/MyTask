package com.youhajun.domain.usecase.room

import com.youhajun.data.models.enums.SocketMessage
import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.models.enums.SocketMessageType
import com.youhajun.domain.usecase.NonSuspendUseCase
import javax.inject.Inject

class SendSocketMsgUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : NonSuspendUseCase<Pair<SocketMessageType, String>, Unit>() {
    override fun invoke(request: Pair<SocketMessageType, String>) {
        roomRepository.sendSocketMessage(SocketMessage.typeOf(request.first.type), request.second)
    }
}