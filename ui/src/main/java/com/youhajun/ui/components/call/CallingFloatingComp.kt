package com.youhajun.ui.components.call

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.youhajun.model_ui.types.webrtc.VideoScreenType
import com.youhajun.model_ui.vo.webrtc.SessionInfoVo
import com.youhajun.model_ui.vo.webrtc.SessionTrackInfoVo
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper

@Composable
fun CallingFloatingComp(
    modifier: Modifier,
    videoScreenType: VideoScreenType,
    fillMaxSessionInfoVo: SessionTrackInfoVo?,
    floatingSessionInfoVo: SessionTrackInfoVo?,
    eglBaseContextWrapper: EglBaseContextWrapper,
    onDoubleTabCallingScreen: (screenType: VideoScreenType, videoTrackId: String?) -> Unit
) {
    fillMaxSessionInfoVo ?: return
    floatingSessionInfoVo ?: return

    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }

    Box(modifier = modifier) {
        CallVideoComp(
                modifier = Modifier.onSizeChanged { parentSize = it }.fillMaxSize().pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = {
                        onDoubleTabCallingScreen(videoScreenType, fillMaxSessionInfoVo.trackVo.videoTrack?.id())
                    })
                },
                trackVo = fillMaxSessionInfoVo.trackVo,
                mediaStateVo = fillMaxSessionInfoVo.callMediaStateHolder,
                eglBaseContextWrapper = eglBaseContextWrapper
            )

        FloatingCallVideoComp(
            modifier = Modifier.size(width = 100.dp, height = 150.dp).pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    onDoubleTabCallingScreen(videoScreenType, floatingSessionInfoVo.trackVo.videoTrack?.id())
                })
            },
            trackVo = floatingSessionInfoVo.trackVo,
            mediaStateVo = floatingSessionInfoVo.callMediaStateHolder,
            eglBaseContextWrapper = eglBaseContextWrapper,
            parentBounds = parentSize,
            paddingValues = PaddingValues(10.dp)
        )
    }
}