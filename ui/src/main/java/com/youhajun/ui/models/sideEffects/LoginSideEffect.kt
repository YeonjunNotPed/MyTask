package com.youhajun.ui.models.sideEffects

sealed class LoginSideEffect {
    data class Toast(val text: String) : LoginSideEffect()
    object GoogleLoginLaunch : LoginSideEffect()
    object KakaoLoginLaunch : LoginSideEffect()
    object HideKeyboard : LoginSideEffect()

    sealed class Navigation: LoginSideEffect() {
        object NavigateUp : Navigation()
    }
}