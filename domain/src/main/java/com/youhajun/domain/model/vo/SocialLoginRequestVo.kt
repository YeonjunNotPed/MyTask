package com.youhajun.domain.model.vo

import com.youhajun.data.model.dto.sign.SocialLoginRequest
import com.youhajun.domain.model.Mapper
import com.youhajun.domain.model.enums.SocialLoginType

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