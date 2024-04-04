package com.youhajun.ui.components.room

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.getstream.webrtc.android.compose.FloatingVideoRenderer
import io.getstream.webrtc.android.compose.VideoRenderer
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

@Composable
fun RoomCallingComp(
    modifier: Modifier,
    myVideoTrack: VideoTrack?,
    partnerVideoTrack: VideoTrack?,
    eglBaseContext: EglBase.Context
) {
    val rendererEvents = object : RendererCommon.RendererEvents {
        override fun onFirstFrameRendered() {

        }

        override fun onFrameResolutionChanged(videoWidth: Int, videoHeight: Int, rotation: Int) {

        }
    }
    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }

    Box {
        Column(
            modifier = modifier.background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (partnerVideoTrack != null && partnerVideoTrack.enabled()) {
                VideoRenderer(
                    videoTrack = partnerVideoTrack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .onSizeChanged { parentSize = it },
                    eglBaseContext = eglBaseContext,
                    rendererEvents = rendererEvents,
                )
            }
        }
        if (myVideoTrack != null && myVideoTrack.enabled()) {
            FloatingVideoRenderer(
                modifier = Modifier
                    .size(width = 150.dp, height = 210.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.BottomEnd),
                videoTrack = myVideoTrack,
                eglBaseContext = eglBaseContext,
                rendererEvents = rendererEvents,
                parentBounds = parentSize,
                paddingValues = PaddingValues(0.dp)
            )
        }
    }
}