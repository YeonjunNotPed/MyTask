package com.youhajun.ui.models.sideEffects

sealed class LiveRoomSideEffect {
    data class Toast(val text: String) : LiveRoomSideEffect()
    sealed class Navigation: LiveRoomSideEffect() {
        object NavigateUp : Navigation()
    }
}