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
import com.youhajun.ui.models.holder.CallMediaStateHolder
import com.youhajun.ui.R
import com.youhajun.ui.models.holder.CallControlActionHolder

@Composable
fun CallBottomComp(
    modifier: Modifier = Modifier,
    mediaStateVo: CallMediaStateHolder,
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
            callMediaStateHolder = mediaStateVo
        ).forEach {
            CallControlItemComp(it, onClickCallAction)
        }
    }
}

@Composable
fun buildCallControlActions(
    callControlActions: List<CallControlAction>,
    callMediaStateHolder: CallMediaStateHolder,
): List<CallControlActionHolder> {
    return callControlActions.map {
        when (it) {
            is CallControlAction.ToggleMicMute -> CallControlActionHolder(
                backgroundColor = if (callMediaStateHolder.isMicMute) Color.White else colorResource(id = R.color.color_292929),
                iconTint = if (callMediaStateHolder.isMicMute) Color.Black else Color.LightGray,
                icon = if (callMediaStateHolder.isMicMute) R.drawable.ic_call_mic_off else R.drawable.ic_call_mic_on,
                callAction = CallControlAction.ToggleMicMute(callMediaStateHolder.isMicMute),
            )

            is CallControlAction.ToggleSpeakerphone -> CallControlActionHolder(
                backgroundColor = if (callMediaStateHolder.isSpeakerEnable) Color.White else colorResource(id = R.color.color_292929),
                iconTint = if (callMediaStateHolder.isSpeakerEnable) Color.Black else Color.LightGray,
                icon = if (callMediaStateHolder.isSpeakerEnable) R.drawable.ic_call_speaker_on else R.drawable.ic_call_speaker_off,
                callAction = CallControlAction.ToggleSpeakerphone(callMediaStateHolder.isSpeakerEnable),
            )

            is CallControlAction.ToggleCamera -> CallControlActionHolder(
                backgroundColor = if (callMediaStateHolder.isCameraEnable) colorResource(id = R.color.color_292929) else Color.White,
                iconTint = if (callMediaStateHolder.isCameraEnable) Color.LightGray else Color.Black,
                icon = if (callMediaStateHolder.isCameraEnable) R.drawable.ic_call_video_on else R.drawable.ic_call_video_off,
                callAction = CallControlAction.ToggleCamera(callMediaStateHolder.isCameraEnable)
            )

            CallControlAction.FlipCamera -> CallControlActionHolder(
                backgroundColor = colorResource(id = R.color.color_292929),
                iconTint = Color.LightGray,
                icon = R.drawable.ic_call_camera_flip,
                callAction = CallControlAction.FlipCamera,
                isEnable = callMediaStateHolder.isCameraEnable
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
