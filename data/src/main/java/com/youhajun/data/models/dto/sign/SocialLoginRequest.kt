package com.youhajun.data.models.dto.sign

import com.google.gson.annotations.SerializedName

data class SocialLoginRequest(
    @SerializedName("socialLoginType")
    val socialLoginType: String,
    @SerializedName("authCode")
    val authCode:String = "",
    @SerializedName("accessToken")
    val accessToken:String = "",
)

