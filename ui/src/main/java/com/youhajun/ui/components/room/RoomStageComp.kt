package com.youhajun.ui.components.room

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.youhajun.model_ui.vo.webrtc.SessionTrackInfoVo
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import com.youhajun.ui.R
import com.youhajun.ui.components.call.MyTaskVideoRenderer
import com.youhajun.ui.components.call.VoiceRecognizerComp
import com.youhajun.ui.components.modifier.infinityProgressCircleBorder

@Composable
fun RoomStageComp(
    modifier: Modifier,
    mySessionVideoInfoVo: SessionTrackInfoVo?,
    eglBaseContextWrapper: EglBaseContextWrapper
) {
    mySessionVideoInfoVo ?: return

    val myMediaStateVo = mySessionVideoInfoVo.callMediaStateHolder

    Box(modifier) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(id = R.string.room_stage_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.W800,
                color = Color.White,
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (myMediaStateVo.isCameraEnable) {
                MyTaskVideoRenderer(
                    trackVo = mySessionVideoInfoVo.trackVo,
                    eglBaseContextWrapper = eglBaseContextWrapper,
                    isFrontCamera = myMediaStateVo.isFrontCamera,
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

        VoiceRecognizerComp(
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp),
            waveModifier = Modifier.width(60.dp).height(45.dp),
            isMicEnable = !myMediaStateVo.isMicMute,
            audioLevels = myMediaStateVo.audioLevelList,
        )
    }
}