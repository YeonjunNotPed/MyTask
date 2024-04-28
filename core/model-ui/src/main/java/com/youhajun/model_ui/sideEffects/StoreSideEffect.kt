package com.youhajun.model_ui.sideEffects

import android.app.Activity

sealed class StoreSideEffect {
    data class Toast(val text: String) : StoreSideEffect()
    data class BillingLaunch(val onBillingLaunch:(Activity) -> Unit): StoreSideEffect()

    sealed class Navigation: StoreSideEffect() {
        data object NavigateUp : Navigation()
        data object NavigateToStoreHistory : Navigation()
    }
}