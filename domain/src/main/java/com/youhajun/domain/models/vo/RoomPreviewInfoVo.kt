package com.youhajun.domain.models.vo

import com.youhajun.data.models.dto.room.RoomPreviewInfo
import com.youhajun.domain.models.Mapper

data class RoomPreviewInfoVo(
    val roomList:List<RoomPreviewVo>,
    val pollingTime: Long
) {
    companion object : Mapper.ResponseMapper<RoomPreviewInfo, RoomPreviewInfoVo> {
        override fun mapDtoToModel(type: RoomPreviewInfo): RoomPreviewInfoVo {
            return type.run {
                RoomPreviewInfoVo(
                    roomList = roomList.map { RoomPreviewVo.mapDtoToModel(it) },
                    pollingTime = pollingTime
                )
            }
        }
    }
}