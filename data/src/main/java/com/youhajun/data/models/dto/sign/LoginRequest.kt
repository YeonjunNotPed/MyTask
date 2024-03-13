package com.youhajun.data.models.dto.sign

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email:String,
    @SerializedName("password")
    val password:String,
)

