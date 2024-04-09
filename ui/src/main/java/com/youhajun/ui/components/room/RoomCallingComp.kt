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
import com.youhajun.ui.utils.webRtc.models.SessionInfoVo
import com.youhajun.ui.utils.webRtc.models.TrackType
import org.webrtc.EglBase

@Composable
fun RoomCallingComp(
    modifier: Modifier,
    mySessionInfoVo: SessionInfoVo?,
    partnerSessionInfoVo: SessionInfoVo?,
    eglBaseContext: EglBase.Context
) {
    val myVideoTrack = mySessionInfoVo?.findTrack(TrackType.VIDEO)?.videoTrack
    val myMediaStateVo = mySessionInfoVo?.callMediaStateVo
    val partnerVideoTrack = partnerSessionInfoVo?.findTrack(TrackType.VIDEO)?.videoTrack
    val partnerMediaStateVo = partnerSessionInfoVo?.callMediaStateVo

    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }

    Box(modifier = modifier.padding(10.dp)) {
        if (partnerVideoTrack != null && partnerMediaStateVo != null) {
            CallVideoComp(
                modifier = Modifier.onSizeChanged { parentSize = it }.fillMaxSize(),
                videoTrack = partnerVideoTrack,
                mediaStateVo = partnerMediaStateVo,
                eglBaseContext = eglBaseContext
            )
        }

        if (myVideoTrack != null && myMediaStateVo != null) {
            FloatingCallVideoComp(
                modifier = Modifier.size(width = 100.dp, height = 150.dp),
                videoTrack = myVideoTrack,
                mediaStateVo = myMediaStateVo,
                eglBaseContext = eglBaseContext,
                parentBounds = parentSize,
                paddingValues = PaddingValues(10.dp)
            )
        }
    }
}