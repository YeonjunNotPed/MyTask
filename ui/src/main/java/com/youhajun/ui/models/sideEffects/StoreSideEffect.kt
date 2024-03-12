package com.youhajun.ui.models.sideEffects

import android.app.Activity

sealed class StoreSideEffect {
    data class Toast(val text: String) : StoreSideEffect()
    data class BillingLaunch(val onBillingLaunch:(Activity) -> Unit): StoreSideEffect()

    sealed class Navigation: StoreSideEffect() {
        object NavigateUp : Navigation()
        object NavigateToStoreHistory : Navigation()
    }
}