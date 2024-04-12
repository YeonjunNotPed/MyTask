package com.youhajun.ui.components.call

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val MAX_AUDIO_LEVEL = 1f

@Composable
fun AudioWaveform(
    modifier: Modifier,
    audioLevels: List<Float>,
    blockColor: Color = Color.Blue,
    blockHeight: Float = 8f,
    animationSpec: AnimationSpec<Float> = tween(500),
    boundaryColor: Color = Color.White,
    boundaryWidth: Float = 1f,
    amplifyFactor: Float = 3f
) {
    val amplifiedAudioLevels = remember(audioLevels) {
        audioLevels.map {
            val amplLevel = it * amplifyFactor
            amplLevel.coerceAtMost(MAX_AUDIO_LEVEL)
        }
    }
    val animatedLevels = amplifiedAudioLevels.map { level ->
        animateFloatAsState(
            targetValue = level,
            animationSpec = animationSpec,
            label = ""
        ).value
    }
    val halfBoundary = remember(boundaryWidth) { boundaryWidth / 2 }

    Canvas(modifier = modifier) {
        val blockWidth = size.width / audioLevels.size

        animatedLevels.forEachIndexed { levelIdx, level ->
            val blockCount = (size.height / blockHeight * level).toInt() + 1

            for (blockIndex in 0 until blockCount) {
                val x = levelIdx * blockWidth
                val y = size.height - blockIndex * blockHeight - blockHeight

                drawRect(
                    color = blockColor,
                    topLeft = Offset(x, y),
                    size = Size(blockWidth, blockHeight)
                )

                drawRect(
                    color = boundaryColor,
                    topLeft = Offset(x, y),
                    size = Size(blockWidth, blockHeight),
                    style = Stroke(width = halfBoundary)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 3)
@Composable
fun PreviewSoundWaveform() {
    AudioWaveform(
        modifier = Modifier
            .width(40.dp)
            .height(40.dp),
        audioLevels = listOf(0.12f, 0.52f, 0.94f, 0.35f, 1f, 0.61f)
    )
}