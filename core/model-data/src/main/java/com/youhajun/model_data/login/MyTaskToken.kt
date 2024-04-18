package com.youhajun.model_data.login

import com.google.gson.annotations.SerializedName

data class MyTaskToken(
    @SerializedName("accessToken")
    private val _accessToken : String?,
    @SerializedName("refreshToken")
    private val _refreshToken : String?
) {
    val accessToken: String
        get() = _accessToken ?: ""
    val refreshToken: String
        get() = _refreshToken ?: ""
}

