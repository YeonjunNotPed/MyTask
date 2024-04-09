package com.youhajun.ui.components.call

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youhajun.domain.models.vo.CallMediaStateVo
import com.youhajun.ui.R
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

@Composable
fun CallVideoComp(
    modifier: Modifier = Modifier,
    videoTrack: VideoTrack,
    mediaStateVo: CallMediaStateVo,
    eglBaseContext: EglBase.Context,
) {

    val rendererEvents = object : RendererCommon.RendererEvents {
        override fun onFirstFrameRendered() {

        }

        override fun onFrameResolutionChanged(videoWidth: Int, videoHeight: Int, rotation: Int) {

        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.color_292929)),
    ) {

        if(mediaStateVo.isCameraEnable) {
            MyTaskVideoRenderer(
                videoTrack = videoTrack,
                modifier = Modifier.fillMaxSize(),
                eglBaseContext = eglBaseContext,
                rendererEvents = rendererEvents,
                isFrontCamera = mediaStateVo.isFrontCamera
            )
        }else {
            Image(
                painter = painterResource(id = R.drawable.img_avatar),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(70.dp)
                    .clip(shape = RoundedCornerShape(24.dp))
            )
        }

        VoiceRecognizerComp(
            modifier = Modifier.align(Alignment.BottomEnd),
            isMicEnable = !mediaStateVo.isMicMute
        )
    }
}
