package com.youhajun.model_ui.holder

import androidx.compose.runtime.Immutable

@Immutable
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

  data object FlipCamera: CallControlAction()
  data object CallingEnd : CallControlAction()
}
