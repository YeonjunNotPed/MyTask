/*
 * Copyright 2023 Stream.IO, Inc. All Rights Reserved.
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

package com.youhajun.ui.utils.webRtc.managers

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import androidx.core.content.getSystemService
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.WebRTCContract.Companion.SESSION_SEPARATOR
import com.youhajun.ui.utils.webRtc.models.TrackType
import com.youhajun.ui.utils.webRtc.models.VideoTrackVo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import dagger.hilt.android.scopes.ViewModelScoped
import org.webrtc.Camera2Capturer
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerationAndroid
import org.webrtc.EglBase
import org.webrtc.MediaStreamTrack
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoCapturer
import org.webrtc.VideoSource
import org.webrtc.VideoTrack
import javax.inject.Inject

@ViewModelScoped
class WebRtcVideoManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val peerConnectionFactory: WebRTCContract.PeerConnectionFactory,
    eglBaseContext: EglBase.Context
) : WebRTCContract.VideoManager {

    private val _videoTrackFlow = MutableStateFlow<HashMap<String, List<VideoTrackVo>>>(hashMapOf())
    override val videoTrackFlow: StateFlow<Map<String, List<VideoTrackVo>>> = _videoTrackFlow

    private val frontCameraId: String by lazy { getFrontCameraElseFirstId() }
    private val videoCapturer: VideoCapturer by lazy {
        Camera2Capturer(context, frontCameraId, null)
    }
    private val cameraManager by lazy { context.getSystemService<CameraManager>() }
    private val cameraEnumerator: Camera2Enumerator by lazy {
        Camera2Enumerator(context)
    }

    private val surfaceTextureHelper = SurfaceTextureHelper.create("SurfaceTextureHelperThread", eglBaseContext)

    private val videoSource: VideoSource by lazy {
        peerConnectionFactory.makeVideoSource(videoCapturer.isScreencast).apply {
            videoCapturer.initialize(surfaceTextureHelper, context, this.capturerObserver)
            videoCapturer.startCapture(resolution.width, resolution.height, 30)
        }
    }

    private val localVideoTrack: VideoTrack by lazy {
        peerConnectionFactory.makeVideoTrack(
            source = videoSource,
            trackId = "${TrackType.VIDEO.type}$SESSION_SEPARATOR${peerConnectionFactory.sessionId}"
        )
    }

    private val resolution: CameraEnumerationAndroid.CaptureFormat
        get() {
            val supportedFormats = cameraEnumerator.getSupportedFormats(frontCameraId) ?: emptyList()
            return findMatchingResolution(supportedFormats)
        }

    override fun flipCamera() {
        (videoCapturer as? Camera2Capturer)?.switchCamera(null)
    }

    override fun enableCamera(enabled: Boolean) {
        if (enabled) {
            videoCapturer.startCapture(resolution.width, resolution.height, 30)
        } else {
            videoCapturer.stopCapture()
        }
    }

    override fun dispose() {
        videoTrackFlow.value.values.forEach { listVo ->
            listVo.forEach { trackVo ->
                trackVo.videoTrack.dispose()
            }
        }

        videoCapturer.stopCapture()
        videoCapturer.dispose()
    }

    override fun addLocalTrackToPeerConnection(addTrack: (MediaStreamTrack) -> Unit) {
        addTrack(localVideoTrack)
        val sessionId = peerConnectionFactory.sessionId
        val videoTrackVo = VideoTrackVo(TrackType.VIDEO, localVideoTrack)
        updateVideoTrackFlow(sessionId, videoTrackVo)
    }

    override fun onVideoTrack(sessionId: String, videoTrackVo: VideoTrackVo) {
        updateVideoTrackFlow(sessionId, videoTrackVo)
    }

    private fun updateVideoTrackFlow(sessionId: String, videoTrackVo: VideoTrackVo) {
        val newMap = HashMap(videoTrackFlow.value)
        val sessionTracks = newMap.getOrDefault(sessionId, emptyList())
        newMap[sessionId] = sessionTracks.toMutableList().apply { add(videoTrackVo) }
        _videoTrackFlow.update { newMap }
    }

    private fun findMatchingResolution(supportedFormats: List<CameraEnumerationAndroid.CaptureFormat>): CameraEnumerationAndroid.CaptureFormat {
        return supportedFormats.find {
            (it.width == 720 || it.width == 480 || it.width == 360)
        } ?: supportedFormats.firstOrNull() ?: error("There is no matched resolution!")
    }

    private fun getFrontCameraElseFirstId(): String {
        val cameraManager =
            cameraManager ?: throw RuntimeException("CameraManager was not initialized!")

        val frontFacingCameraId = findFrontFacingCameraId(cameraManager)
        return frontFacingCameraId ?: getFirstAvailableCameraId(cameraManager)
    }


    private fun findFrontFacingCameraId(cameraManager: CameraManager): String? {
        val cameraIds = cameraManager.cameraIdList

        for (cameraId in cameraIds) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)

            if (lensFacing == CameraMetadata.LENS_FACING_FRONT) {
                return cameraId
            }
        }

        return null
    }

    private fun getFirstAvailableCameraId(cameraManager: CameraManager): String {
        val cameraIds = cameraManager.cameraIdList
        if (cameraIds.isEmpty()) {
            throw RuntimeException("No available cameras.")
        }
        return cameraIds.first()
    }
}