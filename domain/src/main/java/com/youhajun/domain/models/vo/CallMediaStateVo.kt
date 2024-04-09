package com.youhajun.domain.models.vo

data class CallMediaStateVo(
    val isFrontCamera: Boolean = true,
    val isCameraEnable: Boolean = true,
    val isSpeakerEnable: Boolean = false,
    val isMicMute: Boolean = false,
)