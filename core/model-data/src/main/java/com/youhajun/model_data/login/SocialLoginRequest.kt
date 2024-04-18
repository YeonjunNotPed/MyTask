package com.youhajun.model_data.login

import com.google.gson.annotations.SerializedName

data class SocialLoginRequest(
    @SerializedName("socialLoginType")
    val socialLoginType: String,
    @SerializedName("authCode")
    val authCode:String = "",
    @SerializedName("accessToken")
    val accessToken:String = "",
)

