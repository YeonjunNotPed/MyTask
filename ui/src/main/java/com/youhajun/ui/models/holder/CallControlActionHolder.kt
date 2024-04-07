package com.youhajun.ui.models.holder

import androidx.compose.ui.graphics.Color
import com.youhajun.domain.models.sealeds.CallControlAction

data class CallControlActionHolder(
    val backgroundColor: Color,
    val iconTint: Color,
    val icon: Int,
    val callAction: CallControlAction,
    val isEnable: Boolean = true
)