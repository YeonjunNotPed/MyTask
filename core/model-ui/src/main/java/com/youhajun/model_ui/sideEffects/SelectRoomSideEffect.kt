package com.youhajun.model_ui.sideEffects

sealed class SelectRoomSideEffect {
    data class Toast(val text: String) : SelectRoomSideEffect()
    sealed class Navigation: SelectRoomSideEffect() {
        data object NavigateUp : Navigation()
        data class NavigateToLiveRoom(val idx:Long) : Navigation()
    }
}