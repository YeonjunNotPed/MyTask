package com.youhajun.model_ui.wrapper

import androidx.compose.runtime.Immutable
import org.webrtc.EglBase

@Immutable
data class EglBaseContextWrapper(
    val eglContext: EglBase.Context
)