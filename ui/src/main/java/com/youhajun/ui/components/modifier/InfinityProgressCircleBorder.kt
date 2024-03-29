package com.youhajun.ui.components.modifier

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.infinityProgressCircleBorder(
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    borderColor: Color = Color.LightGray,
    progressColor: Color = Color.Red,
    sweepColor:Color = Color.White,
    progressSweep: Float = 300f,
    progressDuration: Int = 5000,
): Modifier {
    val rotateValue = 90f
    val transition = rememberInfiniteTransition(label = "")
    val currentRotation by transition.animateValue(
        -rotateValue,
        targetValue = 360f - rotateValue,
        Float.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = progressDuration,
                easing = LinearEasing
            )
        ), label = ""
    )
    return this then Modifier.drawWithContent {
        val diameterOffset = strokeWidth.toPx() / 2
        val arcDimen = size.width - 2 * diameterOffset

        drawContent()

        drawArc(
            borderColor,
            startAngle = 270f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = Stroke(strokeWidth.toPx())
        )

        rotate(currentRotation) {
            drawArc(
                Brush.sweepGradient(listOf(progressColor, sweepColor)),
                startAngle = 270f + rotateValue,
                sweepAngle = progressSweep,
                useCenter = false,
                topLeft = Offset(diameterOffset, diameterOffset),
                size = Size(arcDimen, arcDimen),
                style = Stroke(strokeWidth.toPx())
            )
        }
    }.padding(strokeWidth)
}