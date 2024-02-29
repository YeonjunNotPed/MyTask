package com.youhajun.ui.graphics.brush

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun getShimmerInfinityAnimationBrush(
    width: Float,
    height: Float,
    duration: Int = 3000,
    repeatMode: RepeatMode = RepeatMode.Reverse,
    color: Color = Color.White,
): Brush {
    val transition = rememberInfiniteTransition(label = "")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = repeatMode
        ), label = ""
    )

    return Brush.linearGradient(
        colors = listOf(
            color,
            Color.White,
            color,
        ),
        start = Offset(x = 0f, y = 0f),
        end = Offset(x = width * progress, y = height * progress),
    )
}