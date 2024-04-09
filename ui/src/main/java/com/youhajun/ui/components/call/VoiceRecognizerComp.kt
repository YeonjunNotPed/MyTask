package com.youhajun.ui.components.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youhajun.ui.R

@Composable
fun VoiceRecognizerComp(
    modifier: Modifier = Modifier,
    isMicEnable:Boolean
) {
    Box(modifier = modifier) {
        if(isMicEnable) {

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
