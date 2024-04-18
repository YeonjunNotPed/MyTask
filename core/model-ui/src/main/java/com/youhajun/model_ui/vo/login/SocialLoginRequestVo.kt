package com.youhajun.model_ui.vo.login

import com.youhajun.model_data.login.SocialLoginRequest
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.types.login.SocialLoginType

data class SocialLoginRequestVo(
    val socialLoginType: SocialLoginType,
    val authCode:String = "",
    val accessToken:String = "",
) {
    companion object : ToDto<SocialLoginRequestVo, SocialLoginRequest> {
        override fun SocialLoginRequestVo.toDto(): SocialLoginRequest = SocialLoginRequest(
            socialLoginType.value,
            authCode,
            accessToken
        )
    }
}