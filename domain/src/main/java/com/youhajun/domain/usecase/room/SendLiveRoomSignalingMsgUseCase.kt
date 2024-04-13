package com.youhajun.domain.usecase.room

import com.youhajun.data.models.enums.SignalingCommand
import com.youhajun.data.repositories.RoomRepository
import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.domain.usecase.NonSuspendUseCase
import javax.inject.Inject

class SendLiveRoomSignalingMsgUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) : NonSuspendUseCase<Pair<SignalingType, String>, Unit>() {
    override fun invoke(request: Pair<SignalingType, String>) {
        roomRepository.sendSignalingMessage(SignalingCommand.typeOf(request.first.type), request.second)
    }
}