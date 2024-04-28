package com.youhajun.model_ui.sideEffects

sealed class LiveRoomSideEffect {
    data class Toast(val text: String) : LiveRoomSideEffect()

    data class LivePermissionLauncher(val granted: () -> Unit) : LiveRoomSideEffect()

    sealed class Navigation : LiveRoomSideEffect() {
        data object NavigateUp : Navigation()
    }
}