package com.youhajun.ui.components.room

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.ui.R
import com.youhajun.ui.components.modifier.infinityProgressCircleBorder
import com.youhajun.ui.utils.webRtc.models.VideoTrackVo
import io.getstream.webrtc.android.compose.VideoRenderer
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.VideoTrack

@Composable
fun RoomStageComp(
    modifier: Modifier,
    myVideoTrackVo: VideoTrackVo?,
    eglBaseContext: EglBase.Context
) {
    val rendererEvents = object : RendererCommon.RendererEvents {
        override fun onFirstFrameRendered() {

        }

        override fun onFrameResolutionChanged(videoWidth: Int, videoHeight: Int, rotation: Int) {

        }
    }

    Column(
        modifier = modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.room_stage_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.W800,
            color = Color.Black,
            modifier = Modifier.wrapContentSize()
        )

        if (myVideoTrackVo != null && myVideoTrackVo.isCameraEnabled) {
            VideoRenderer(
                videoTrack = myVideoTrackVo.videoTrack,
                eglBaseContext = eglBaseContext,
                rendererEvents = rendererEvents,
                modifier = Modifier
                    .size(200.dp)
                    .clip(shape = CircleShape)
                    .infinityProgressCircleBorder(
                        progressColor = Color.Blue,
                        borderColor = Color.White,
                        progressSweep = 360f,
                        strokeWidth = 5.dp
                    )
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.img_avatar),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(200.dp)
                    .clip(shape = CircleShape)
                    .infinityProgressCircleBorder(
                        progressColor = Color.Blue,
                        borderColor = Color.White,
                        progressSweep = 360f,
                        strokeWidth = 5.dp
                    )
            )
        }
    }
}