package com.youhajun.ui.components.room

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.youhajun.model_ui.types.webrtc.VideoScreenType
import com.youhajun.model_ui.vo.webrtc.SessionTrackInfoVo
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import com.youhajun.ui.components.call.CallingFloatingComp
import com.youhajun.ui.components.call.CallingSplitComp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RoomCallingComp(
    modifier: Modifier,
    screenType: VideoScreenType,
    sessionFlatInfoList: ImmutableList<SessionTrackInfoVo>,
    fillMaxSessionTrackInfo: SessionTrackInfoVo?,
    floatingSessionTrackInfo: SessionTrackInfoVo?,
    eglBaseContextWrapper: EglBaseContextWrapper,
    onDoubleTabCallingScreen: (screenType: VideoScreenType, trackId: String?) -> Unit
) {

    when(screenType) {
        VideoScreenType.SPLIT -> {
            CallingSplitComp(
                modifier = modifier,
                sessionTrackInfoList = sessionFlatInfoList,
                eglBaseContextWrapper = eglBaseContextWrapper,
                onDoubleTabCallingScreen = onDoubleTabCallingScreen
            )
        }
        VideoScreenType.FLOATING -> {
            CallingFloatingComp(
                modifier = modifier,
                videoScreenType = screenType,
                fillMaxSessionInfoVo = fillMaxSessionTrackInfo,
                floatingSessionInfoVo = floatingSessionTrackInfo,
                eglBaseContextWrapper = eglBaseContextWrapper,
                onDoubleTabCallingScreen = onDoubleTabCallingScreen,
            )
        }
    }
}