package com.youhajun.domain.models.sealeds

sealed class CallControlAction {
  data class ToggleSpeakerphone(
    val isEnabled: Boolean
  ) : CallControlAction()

  data class ToggleCamera(
    val isEnabled: Boolean
  ) : CallControlAction()

  data class ToggleMicMute(
    val isMute: Boolean
  ) : CallControlAction()

  object FlipCamera: CallControlAction()
  object CallingEnd : CallControlAction()
}
