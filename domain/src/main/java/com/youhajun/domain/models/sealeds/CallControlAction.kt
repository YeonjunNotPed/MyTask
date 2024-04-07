package com.youhajun.domain.models.sealeds

sealed class CallControlAction {
  data class ToggleMicroPhone(
    val isEnabled: Boolean
  ) : CallControlAction()

  data class ToggleCamera(
    val isEnabled: Boolean
  ) : CallControlAction()

  object FlipCamera: CallControlAction()
  object CallingEnd : CallControlAction()
}
