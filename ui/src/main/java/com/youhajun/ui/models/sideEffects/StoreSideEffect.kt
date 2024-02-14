package com.youhajun.ui.models.sideEffects

sealed class StoreSideEffect {
    data class Toast(val text: String) : StoreSideEffect()

    sealed class Navigation: StoreSideEffect() {
        object NavigateToStoreHistory : Navigation()
    }
}