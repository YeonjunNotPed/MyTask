package com.youhajun.ui.models.holder

data class CallMediaStateHolder(
    val audioLevelList: List<Float> = emptyList(),
    val isFrontCamera: Boolean = true,
    val isCameraEnable: Boolean = true,
    val isSpeakerEnable: Boolean = false,
    val isMicMute: Boolean = false,
)