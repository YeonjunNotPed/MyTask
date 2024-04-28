package com.youhajun.model_ui.sideEffects

sealed class LoginSideEffect {
    data class Toast(val text: String) : LoginSideEffect()
    data object GoogleLoginLaunch : LoginSideEffect()
    data object KakaoLoginLaunch : LoginSideEffect()
    data object HideKeyboard : LoginSideEffect()

    sealed class Navigation: LoginSideEffect() {
        data object NavigateUp : Navigation()
    }
}