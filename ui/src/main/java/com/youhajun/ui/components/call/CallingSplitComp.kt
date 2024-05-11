package com.youhajun.ui.components.call

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.youhajun.model_ui.types.webrtc.VideoScreenType
import com.youhajun.model_ui.vo.webrtc.SessionTrackInfoVo
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CallingSplitComp(
    modifier: Modifier,
    sessionTrackInfoList: ImmutableList<SessionTrackInfoVo>,
    eglBaseContextWrapper: EglBaseContextWrapper,
    onDoubleTabCallingScreen: (screenType: VideoScreenType, videoTrackId: String?) -> Unit
) {
    val gridColumns = if (sessionTrackInfoList.size <= 2) GridCells.Fixed(1) else GridCells.Fixed(2)

    LazyVerticalGrid(
        columns = gridColumns,
        modifier = modifier
    ) {
        items(sessionTrackInfoList, key = { it.trackVo.videoTrack?.id() ?: it }) { sessionTrackInfoVo ->
            CallVideoComp(
                modifier = Modifier.fillMaxWidth().pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = {
                        onDoubleTabCallingScreen(VideoScreenType.SPLIT, sessionTrackInfoVo.trackVo.videoTrack?.id())
                    })
                },
                trackVo = sessionTrackInfoVo.trackVo,
                mediaStateVo = sessionTrackInfoVo.callMediaStateHolder,
                eglBaseContextWrapper = eglBaseContextWrapper,
            )
        }
    }
}