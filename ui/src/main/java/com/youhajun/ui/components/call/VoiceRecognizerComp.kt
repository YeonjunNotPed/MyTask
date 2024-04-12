package com.youhajun.ui.components.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.youhajun.ui.R

@Composable
fun VoiceRecognizerComp(
    modifier: Modifier = Modifier,
    waveModifier: Modifier,
    isMicEnable:Boolean,
    audioLevels: List<Float>,
) {
    Box(modifier = modifier) {
        if(isMicEnable) {
            AudioWaveform(modifier = waveModifier, audioLevels = audioLevels)
        } else {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            ) {
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center),
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_call_mic_off),
                    contentDescription = null
                )
            }
        }
    }
}
