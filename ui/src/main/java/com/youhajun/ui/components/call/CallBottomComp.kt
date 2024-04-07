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
                CallControlAction.ToggleCamera(mediaStateVo.isCameraEnable),
                CallControlAction.ToggleMicroPhone(mediaStateVo.isMicEnable),
                CallControlAction.FlipCamera,
                CallControlAction.CallingEnd,
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
            is CallControlAction.ToggleMicroPhone -> CallControlActionHolder(
                backgroundColor = if (callMediaStateVo.isMicEnable) colorResource(id = R.color.color_292929) else Color.White,
                iconTint = if (callMediaStateVo.isMicEnable) Color.LightGray else Color.Black,
                icon = if (callMediaStateVo.isMicEnable) R.drawable.ic_call_mic_on else R.drawable.ic_call_mic_off,
                callAction = CallControlAction.ToggleMicroPhone(callMediaStateVo.isMicEnable),
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
