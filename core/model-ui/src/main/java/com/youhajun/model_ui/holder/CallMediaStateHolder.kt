package com.youhajun.model_ui.holder

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CallMediaStateHolder(
    val audioLevelList: ImmutableList<Float> = persistentListOf(),
    val isFrontCamera: Boolean = true,
    val isCameraEnable: Boolean = true,
    val isSpeakerEnable: Boolean = false,
    val isMicMute: Boolean = false,
)