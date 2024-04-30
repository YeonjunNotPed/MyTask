package com.youhajun.ui.components.call

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youhajun.model_ui.holder.CallMediaStateHolder
import com.youhajun.ui.R
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import com.youhajun.model_ui.vo.webrtc.TrackVo
import org.webrtc.RendererCommon

@Composable
fun CallVideoComp(
    modifier: Modifier = Modifier,
    trackVo: TrackVo,
    mediaStateVo: CallMediaStateHolder,
    eglBaseContextWrapper: EglBaseContextWrapper
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
                trackVo = trackVo,
                modifier = Modifier.fillMaxSize(),
                eglBaseContextWrapper = eglBaseContextWrapper,
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
            waveModifier = Modifier.width(60.dp).height(45.dp),
            isMicEnable = !mediaStateVo.isMicMute,
            audioLevels = mediaStateVo.audioLevelList
        )
    }
}
