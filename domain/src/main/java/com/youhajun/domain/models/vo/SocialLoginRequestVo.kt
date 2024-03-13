package com.youhajun.domain.models.vo

import com.youhajun.data.models.dto.sign.SocialLoginRequest
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.SocialLoginType

data class SocialLoginRequestVo(
    val socialLoginType: SocialLoginType,
    val authCode:String = "",
    val accessToken:String = "",
) {
    companion object : Mapper.RequestMapper<SocialLoginRequest, SocialLoginRequestVo> {
        override fun mapModelToDto(type: SocialLoginRequestVo): SocialLoginRequest {
            return type.run {
                SocialLoginRequest(
                    socialLoginType.value,
                    authCode,
                    accessToken
                )
            }
        }
    }
}