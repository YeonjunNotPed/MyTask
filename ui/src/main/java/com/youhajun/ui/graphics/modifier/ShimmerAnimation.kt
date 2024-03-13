package com.youhajun.ui.graphics.modifier

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.youhajun.ui.graphics.shape.badgePath

@Composable
fun Modifier.shimmerBadgeModifier(
    color: Color,
    cornerRadius: Float = 24f,
    duration: Int = 4000,
    repeatMode: RepeatMode = RepeatMode.Reverse,
): Modifier {
    val transition = rememberInfiniteTransition(label = "infinityAnime")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = repeatMode
        ), label = "shimmerAnime"
    )

    return this then Modifier
        .drawBehind {
            val width = size.width
            val height = size.height
            val startX = width * progress
            val endX = width * progress - width
            val startY = height * progress
            val endY = height * progress - height

            val shimmerBrush = Brush.linearGradient(
                colors = listOf(
                    color,
                    color,
                    Color.White,
                    color,
                    color,
                ),
                start = Offset(startX, startY),
                end = Offset(endX,endY),
            )
            val path = Path().apply {
                badgePath(width, height, cornerRadius)
            }
            drawPath(path, shimmerBrush)
        }.padding(start = 2.dp, end = 24.dp, top = 1.dp, bottom = 1.dp)
}