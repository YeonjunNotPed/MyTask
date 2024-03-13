package com.youhajun.domain.models.vo

import com.youhajun.data.models.dto.sign.LoginRequest
import com.youhajun.domain.models.Mapper

data class LoginRequestVo(
    val email: String,
    val password: String,
) {
    companion object : Mapper.RequestMapper<LoginRequest, LoginRequestVo> {
        override fun mapModelToDto(type: LoginRequestVo): LoginRequest {
            return type.run {
                LoginRequest(
                    email,
                    password
                )
            }
        }
    }
}