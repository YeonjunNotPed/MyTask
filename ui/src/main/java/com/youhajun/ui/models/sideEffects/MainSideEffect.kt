package com.youhajun.ui.models.sideEffects

sealed class MainSideEffect {
    data class Toast(val text: String) : MainSideEffect()
}