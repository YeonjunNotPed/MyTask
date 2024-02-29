package com.youhajun.ui.models.sideEffects

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult

sealed class StoreSideEffect {
    data class Toast(val text: String) : StoreSideEffect()
    data class BillingLaunch(val onBillingLaunch:(Activity) -> Unit): StoreSideEffect()

    sealed class Navigation: StoreSideEffect() {
        object NavigateToStoreHistory : Navigation()
    }
}