package com.youhajun.ui.components.room

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.youhajun.ui.components.call.CallVideoComp
import com.youhajun.ui.components.call.FloatingCallVideoComp
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import com.youhajun.model_ui.vo.webrtc.SessionInfoVo
import com.youhajun.model_ui.types.webrtc.TrackType

@Composable
fun RoomCallingComp(
    modifier: Modifier,
    mySessionInfoVo: SessionInfoVo?,
    partnerSessionInfoVo: SessionInfoVo?,
    eglBaseContextWrapper: EglBaseContextWrapper
) {
    val myVideoTrackVo = mySessionInfoVo?.findTrack(TrackType.VIDEO)
    val myMediaStateVo = mySessionInfoVo?.callMediaStateHolder
    val partnerVideoTrackVo = partnerSessionInfoVo?.findTrack(TrackType.VIDEO)
    val partnerMediaStateVo = partnerSessionInfoVo?.callMediaStateHolder

    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }

    Box(modifier = modifier.padding(10.dp)) {
        if (partnerVideoTrackVo != null && partnerMediaStateVo != null) {
            CallVideoComp(
                modifier = Modifier.onSizeChanged { parentSize = it }.fillMaxSize(),
                trackVo = partnerVideoTrackVo,
                mediaStateVo = partnerMediaStateVo,
                eglBaseContextWrapper = eglBaseContextWrapper
            )
        }

        if (myVideoTrackVo != null && myMediaStateVo != null) {
            FloatingCallVideoComp(
                modifier = Modifier.size(width = 100.dp, height = 150.dp),
                trackVo = myVideoTrackVo,
                mediaStateVo = myMediaStateVo,
                eglBaseContextWrapper = eglBaseContextWrapper,
                parentBounds = parentSize,
                paddingValues = PaddingValues(10.dp)
            )
        }
    }
}