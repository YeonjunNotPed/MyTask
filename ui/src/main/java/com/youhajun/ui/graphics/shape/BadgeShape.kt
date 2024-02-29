package com.youhajun.ui.graphics.shape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BadgeShape(private val cornerRadius: Float = 24f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            badgePath(size.width, size.height, cornerRadius)
        })
    }
}

fun Path.badgePath(width: Float, height: Float, cornerRadius: Float) {
    arcTo(
        rect = Rect(
            left = 0f,
            top = 0f,
            right = cornerRadius,
            bottom = cornerRadius
        ),
        startAngleDegrees = 180.0f,
        sweepAngleDegrees = 90.0f,
        forceMoveTo = false
    )
    lineTo(width, 0f)
    cubicTo(width, 0f, width / 2, height / 2, width, height)
    arcTo(
        rect = Rect(
            left = 0f,
            top = height - cornerRadius,
            right = cornerRadius,
            bottom = height
        ),
        startAngleDegrees = 90.0f,
        sweepAngleDegrees = 90.0f,
        forceMoveTo = false
    )
    close()
}