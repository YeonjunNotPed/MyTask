package com.youhajun.ui.utils.audio.model

import java.nio.ByteBuffer

data class AudioSampleVo(
    val sampleRate: Int,
    val channelCount: Int,
    val audioFormat: Int,
    val audioData: ByteBuffer
)