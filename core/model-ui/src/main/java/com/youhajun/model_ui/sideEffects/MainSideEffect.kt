package com.youhajun.model_ui.sideEffects

sealed class MainSideEffect {
    data class Toast(val text: String) : MainSideEffect()
}