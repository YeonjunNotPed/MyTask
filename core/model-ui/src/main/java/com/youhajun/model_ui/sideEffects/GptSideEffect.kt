package com.youhajun.model_ui.sideEffects

sealed class GptSideEffect {
    data class Toast(val text: String) : GptSideEffect()
    data object HideKeyboard : GptSideEffect()
    data object DrawerMenuOpen : GptSideEffect()
    data object DrawerMenuClose : GptSideEffect()
    data class RunTypingAnimation(val targetIdx: Long) : GptSideEffect()
    sealed class Navigation: GptSideEffect() {
        data object NavigateUp : Navigation()
    }
}