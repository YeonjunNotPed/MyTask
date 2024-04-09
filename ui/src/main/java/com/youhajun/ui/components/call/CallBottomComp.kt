package com.youhajun.ui.components.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.youhajun.domain.models.sealeds.CallControlAction
import com.youhajun.domain.models.vo.CallMediaStateVo
import com.youhajun.ui.R
import com.youhajun.ui.models.holder.CallControlActionHolder

@Composable
fun CallBottomComp(
    modifier: Modifier = Modifier,
    mediaStateVo: CallMediaStateVo,
    onClickCallAction: (CallControlAction) -> Unit
) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        buildCallControlActions(
            listOf(
                CallControlAction.ToggleSpeakerphone(mediaStateVo.isSpeakerEnable),
                CallControlAction.ToggleMicMute(mediaStateVo.isMicMute),
                CallControlAction.CallingEnd,
                CallControlAction.FlipCamera,
                CallControlAction.ToggleCamera(mediaStateVo.isCameraEnable),
            ),
            callMediaStateVo = mediaStateVo
        ).forEach {
            CallControlItemComp(it, onClickCallAction)
        }
    }
}

@Composable
fun buildCallControlActions(
    callControlActions: List<CallControlAction>,
    callMediaStateVo: CallMediaStateVo,
): List<CallControlActionHolder> {
    return callControlActions.map {
        when (it) {
            is CallControlAction.ToggleMicMute -> CallControlActionHolder(
                backgroundColor = if (callMediaStateVo.isMicMute) Color.White else colorResource(id = R.color.color_292929),
                iconTint = if (callMediaStateVo.isMicMute) Color.Black else Color.LightGray,
                icon = if (callMediaStateVo.isMicMute) R.drawable.ic_call_mic_off else R.drawable.ic_call_mic_on,
                callAction = CallControlAction.ToggleMicMute(callMediaStateVo.isMicMute),
            )

            is CallControlAction.ToggleSpeakerphone -> CallControlActionHolder(
                backgroundColor = if (callMediaStateVo.isSpeakerEnable) Color.White else colorResource(id = R.color.color_292929),
                iconTint = if (callMediaStateVo.isSpeakerEnable) Color.Black else Color.LightGray,
                icon = if (callMediaStateVo.isSpeakerEnable) R.drawable.ic_call_speaker_on else R.drawable.ic_call_speaker_off,
                callAction = CallControlAction.ToggleSpeakerphone(callMediaStateVo.isSpeakerEnable),
            )

            is CallControlAction.ToggleCamera -> CallControlActionHolder(
                backgroundColor = if (callMediaStateVo.isCameraEnable) colorResource(id = R.color.color_292929) else Color.White,
                iconTint = if (callMediaStateVo.isCameraEnable) Color.LightGray else Color.Black,
                icon = if (callMediaStateVo.isCameraEnable) R.drawable.ic_call_video_on else R.drawable.ic_call_video_off,
                callAction = CallControlAction.ToggleCamera(callMediaStateVo.isCameraEnable)
            )

            CallControlAction.FlipCamera -> CallControlActionHolder(
                backgroundColor = colorResource(id = R.color.color_292929),
                iconTint = Color.LightGray,
                icon = R.drawable.ic_call_camera_flip,
                callAction = CallControlAction.FlipCamera,
                isEnable = callMediaStateVo.isCameraEnable
            )

            CallControlAction.CallingEnd -> CallControlActionHolder(
                backgroundColor = Color.Red,
                iconTint = Color.White,
                icon = R.drawable.ic_call_end,
                callAction = CallControlAction.CallingEnd
            )
        }
    }
}
