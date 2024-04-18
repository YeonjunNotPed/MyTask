package com.youhajun.model_data.room

import com.google.gson.annotations.SerializedName

data class RoomPreviewInfo(
    @SerializedName("roomList")
    private val _roomList: List<RoomPreview>?,
    @SerializedName("pollingTime")
    private val _pollingTime: Long?,
) {
    val roomList: List<RoomPreview>
        get() = _roomList ?: emptyList()
    val pollingTime: Long
        get() = _pollingTime ?: -1
}