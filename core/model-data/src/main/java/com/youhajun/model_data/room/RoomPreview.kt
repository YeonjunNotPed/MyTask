package com.youhajun.model_data.room

import com.google.gson.annotations.SerializedName

data class RoomPreview(
    @SerializedName("idx")
    private val _idx: Long?,
    @SerializedName("roomType")
    private val _roomType: String?,
    @SerializedName("title")
    private val _title: String?,
    @SerializedName("content")
    private val _content: String?,
    @SerializedName("createAt")
    private val _createAt: String?,
    @SerializedName("matchingStandardNumber")
    private val _matchingStandardNumber: Int?,
    @SerializedName("currentPeopleNumber")
    private val _currentPeopleNumber: Int?,
) {
    val idx: Long
        get() = _idx ?: -1
    val roomType: String
        get() = _roomType ?: ""
    val title: String
        get() = _title ?: ""
    val content: String
        get() = _content ?: ""
    val createAt: String
        get() = _createAt ?: ""
    val matchingStandardNumber: Int
        get() = _matchingStandardNumber ?: -1
    val currentPeopleNumber: Int
        get() = _currentPeopleNumber ?: -1
}