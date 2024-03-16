package com.youhajun.ui.models.sideEffects

sealed class SelectRoomSideEffect {
    data class Toast(val text: String) : SelectRoomSideEffect()
    sealed class Navigation: SelectRoomSideEffect() {
        object NavigateUp : Navigation()
        data class NavigateToLiveRoom(val idx:Long) : Navigation()
    }
}