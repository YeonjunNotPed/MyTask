package com.youhajun.ui.models.sideEffects

sealed class GptSideEffect {
    data class Toast(val text: String) : GptSideEffect()
    sealed class Navigation: GptSideEffect() {
        object NavigateUp : Navigation()
    }
}