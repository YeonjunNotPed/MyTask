package com.youhajun.ui.models.sideEffects

sealed class LiveRoomSideEffect {
    data class Toast(val text: String) : LiveRoomSideEffect()

    data class LivePermissionLauncher(val granted: () -> Unit) : LiveRoomSideEffect()

    sealed class Navigation : LiveRoomSideEffect() {
        object NavigateUp : Navigation()
    }
}