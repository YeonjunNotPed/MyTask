package com.youhajun.data.model.dto.auth

import com.google.gson.annotations.SerializedName

data class MyTaskToken(
    @SerializedName("accessToken")
    val accessToken : String = "",
    @SerializedName("refreshToken")
    val refreshToken : String = ""
)

