package com.youhajun.ui.components.call

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.youhajun.model_ui.holder.CallMediaStateHolder
import com.youhajun.model_ui.vo.webrtc.TrackVo
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import com.youhajun.ui.R

@Composable
fun FloatingCallVideoComp(
    modifier: Modifier = Modifier,
    trackVo: TrackVo,
    mediaStateVo: CallMediaStateHolder,
    eglBaseContextWrapper: EglBaseContextWrapper,
    parentBounds: IntSize,
    paddingValues: PaddingValues = PaddingValues(0.dp, 0.dp)
) {
    var currentCompSize by remember { mutableStateOf(IntSize(0, 0)) }
    var dragPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var dragAlign by remember { mutableStateOf(Alignment.BottomEnd) }
    val offset by animateOffsetAsState(targetValue = dragPosition, label = "")

    val startPadding = paddingValues.calculateStartPadding(LayoutDirection.Ltr).value
    val endPadding = paddingValues.calculateStartPadding(LayoutDirection.Ltr).value
    val topPadding = paddingValues.calculateTopPadding().value
    val bottomPadding = paddingValues.calculateBottomPadding().value
    val boundaryWidth = parentBounds.width - currentCompSize.width - endPadding
    val boundaryHeight = parentBounds.height - currentCompSize.height - bottomPadding

    val topStartPosition = Offset(startPadding, topPadding)
    val topEndPosition = Offset(boundaryWidth, topPadding)
    val bottomStartPosition = Offset(startPadding, boundaryHeight)
    val bottomEndPosition = Offset(boundaryWidth, boundaryHeight)

    LaunchedEffect(parentBounds) {
        dragPosition = calculateDragPosition(
            dragAlign,
            topStartPosition,
            topEndPosition,
            bottomStartPosition,
            bottomEndPosition
        )
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(offset.x.toInt(), offset.y.toInt())
            }
            .pointerInput(parentBounds) {
                this.detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragPosition =
                            Offset(dragPosition.x + dragAmount.x, dragPosition.y + dragAmount.y)
                    },
                    onDragEnd = {
                        val align = calculateDragDropAlign(dragPosition, parentBounds)
                        val position = calculateDragPosition(
                            align,
                            topStartPosition,
                            topEndPosition,
                            bottomStartPosition,
                            bottomEndPosition
                        )
                        dragAlign = align
                        dragPosition = position
                    },
                    onDragCancel = {
                        dragPosition = calculateDragPosition(
                            dragAlign,
                            topStartPosition,
                            topEndPosition,
                            bottomStartPosition,
                            bottomEndPosition
                        )
                    }
                )
            }
            .then(modifier)
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.color_292929))
            .onGloballyPositioned { currentCompSize = it.size },
    ) {

        if (mediaStateVo.isCameraEnable) {
            MyTaskVideoRenderer(
                trackVo = trackVo,
                modifier = Modifier.fillMaxSize(),
                eglBaseContextWrapper = eglBaseContextWrapper,
                isFrontCamera = mediaStateVo.isFrontCamera
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.img_avatar),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(35.dp)
                    .clip(shape = RoundedCornerShape(24.dp))
            )
        }

        VoiceRecognizerComp(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(3.dp),
            waveModifier = Modifier
                .width(60.dp)
                .height(45.dp),
            isMicEnable = !mediaStateVo.isMicMute,
            audioLevels = mediaStateVo.audioLevelList
        )
    }
}

private fun calculateDragPosition(
    dragAlign: Alignment,
    topStartPosition: Offset,
    topEndPosition: Offset,
    bottomStartPosition: Offset,
    bottEndPosition: Offset
): Offset {
    return when (dragAlign) {
        Alignment.TopStart -> topStartPosition
        Alignment.TopEnd -> topEndPosition
        Alignment.BottomStart -> bottomStartPosition
        Alignment.BottomEnd -> bottEndPosition
        else -> bottEndPosition
    }
}

private fun calculateDragDropAlign(dragPosition: Offset, parentBounds: IntSize): Alignment {
    val parentHalfWidth = parentBounds.width / 2
    val parentHalfHeight = parentBounds.height / 2

    val topStart = dragPosition.x < parentHalfWidth && dragPosition.y < parentHalfHeight
    val topEnd = dragPosition.x > parentHalfWidth && dragPosition.y < parentHalfHeight
    val bottomStart = dragPosition.x < parentHalfWidth && dragPosition.y > parentHalfHeight
    val bottomEnd = dragPosition.x > parentHalfWidth && dragPosition.y > parentHalfHeight

    return when {
        topStart -> Alignment.TopStart
        topEnd -> Alignment.TopEnd
        bottomStart -> Alignment.BottomStart
        bottomEnd -> Alignment.BottomEnd
        else -> Alignment.BottomEnd
    }
}