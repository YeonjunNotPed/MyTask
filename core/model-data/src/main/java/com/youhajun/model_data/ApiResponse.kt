package com.youhajun.model_data

import com.google.gson.annotations.SerializedName

data class ApiResponse<DTO: Any>(
    @SerializedName("data")
    val data:DTO? = null,
    @SerializedName("message")
    val message:String = "",
    @SerializedName("statusCode")
    val statusCode:Int = -1,
)