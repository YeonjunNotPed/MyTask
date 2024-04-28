package com.youhajun.model_ui.holder

import androidx.compose.ui.graphics.Color

data class CallControlActionHolder(
    val backgroundColor: Color,
    val iconTint: Color,
    val icon: Int,
    val callAction: CallControlAction,
    val isEnable: Boolean = true
)