package com.youhajun.data.model.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<DTO>(
    @SerializedName("data")
    val data:DTO? = null,
    @SerializedName("message")
    val message:String = "",
    @SerializedName("statusCode")
    val statusCode:Int = -1,
)