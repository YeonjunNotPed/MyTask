package com.youhajun.model_ui.vo.login

import com.youhajun.model_data.login.LoginRequest
import com.youhajun.model_data.login.MyTaskToken
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.ToModel

data class MyTaskTokenVo(
    val accessToken : String,
    val refreshToken : String
) {
    companion object : ToModel<MyTaskTokenVo, MyTaskToken> {
        override fun MyTaskToken.toModel(): MyTaskTokenVo = MyTaskTokenVo(
            accessToken,
            refreshToken
        )
    }
}
