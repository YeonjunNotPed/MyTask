package com.youhajun.domain.model.vo

import com.youhajun.data.model.dto.sign.LoginRequest
import com.youhajun.domain.model.Mapper

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