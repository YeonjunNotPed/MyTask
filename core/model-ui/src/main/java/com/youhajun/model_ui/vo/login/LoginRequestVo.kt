package com.youhajun.model_ui.vo.login

import com.youhajun.model_data.login.LoginRequest
import com.youhajun.model_ui.ToDto

data class LoginRequestVo(
    val email: String,
    val password: String,
) {
    companion object : ToDto<LoginRequestVo, LoginRequest> {
        override fun LoginRequestVo.toDto(): LoginRequest = LoginRequest(
            email,
            password
        )
    }
}