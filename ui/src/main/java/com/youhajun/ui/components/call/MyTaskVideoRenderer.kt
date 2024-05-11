/*
 * Copyright (c) 2014-2023 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.youhajun.ui.components.call

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.youhajun.model_ui.types.webrtc.VideoScalingType
import com.youhajun.model_ui.types.webrtc.VideoScalingType.Companion.toCommonScalingType
import com.youhajun.model_ui.vo.webrtc.TrackVo
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import io.getstream.webrtc.android.ui.VideoTextureViewRenderer
import org.webrtc.RendererCommon.RendererEvents
import org.webrtc.VideoTrack

/**
 * Renders a single video track based on the call state.
 *
 * @param videoTrack The track containing the video stream for a given participant.
 * @param modifier Modifier for styling.
 */
@Composable
fun MyTaskVideoRenderer(
    modifier: Modifier = Modifier,
    trackVo: TrackVo,
    eglBaseContextWrapper: EglBaseContextWrapper,
    videoScalingType: VideoScalingType = VideoScalingType.SCALE_ASPECT_BALANCED,
    onTextureViewCreated: (VideoTextureViewRenderer) -> Unit = { },
    onFirstFrameRendered: () -> Unit = { },
    onFrameResolutionChanged: (Int, Int, Int) -> Unit = { _, _, _ -> },
    isFrontCamera: Boolean
) {
    val videoTrack = trackVo.videoTrack ?: return
    val trackState: MutableState<VideoTrack?> = remember { mutableStateOf(null) }
    var view: VideoTextureViewRenderer? by remember { mutableStateOf(null) }

    val rendererEvents by remember { mutableStateOf(object : RendererEvents {
        override fun onFirstFrameRendered() {
            onFirstFrameRendered()
        }
        override fun onFrameResolutionChanged(videoWidth: Int, videoHeight: Int, rotation: Int) {
            onFrameResolutionChanged(videoWidth, videoHeight, rotation)
        }
    }
    )}

    DisposableEffect(videoTrack) {
        onDispose {
            cleanTrack(view, trackState)
        }
    }

    AndroidView(
        factory = { context ->
            VideoTextureViewRenderer(context).apply {
                init(eglBaseContextWrapper.eglContext, rendererEvents)
                setScalingType(scalingType = videoScalingType.toCommonScalingType())
                setupVideo(trackState, videoTrack, this)
                onTextureViewCreated.invoke(this)
                view = this
            }
        },
        update = { v ->
            setupVideo(trackState, videoTrack, v)
            v.setMirror(isFrontCamera)
        },
        modifier = modifier,
    )
}

private fun cleanTrack(
    view: VideoTextureViewRenderer?,
    trackState: MutableState<VideoTrack?>,
) {
    view?.let { trackState.value?.removeSink(it) }
    trackState.value = null
}

private fun setupVideo(
    trackState: MutableState<VideoTrack?>,
    track: VideoTrack,
    renderer: VideoTextureViewRenderer,
) {
    if (trackState.value == track) {
        return
    }

    cleanTrack(renderer, trackState)

    trackState.value = track
    track.addSink(renderer)
}
