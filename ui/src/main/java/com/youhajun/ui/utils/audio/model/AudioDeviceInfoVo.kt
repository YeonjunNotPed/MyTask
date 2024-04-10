package com.youhajun.ui.utils.audio.model

data class AudioDeviceInfoVo(
    val availableDeviceList: List<AudioDeviceType> = emptyList(),
    val currentAudioDevice: AudioDeviceType? = null
)