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

import com.youhajun.ui.utils.audio.AudioController
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.WebRTCContract.Companion.ID_SEPARATOR
import com.youhajun.ui.utils.webRtc.models.TrackType
import dagger.hilt.android.scopes.ViewModelScoped
import org.webrtc.AudioTrack
import org.webrtc.MediaConstraints
import org.webrtc.MediaStreamTrack
import javax.inject.Inject

@ViewModelScoped
class WebRtcAudioManager @Inject constructor(
    private val peerConnectionFactory: WebRTCContract.PeerConnectionFactory,
    private val audioController: AudioController
) : WebRTCContract.AudioManager {

    private val audioConstraints: MediaConstraints by lazy {
        buildAudioConstraints()
    }

    private val audioSource by lazy {
        peerConnectionFactory.makeAudioSource(audioConstraints)
    }

    private val localAudioTrack: AudioTrack by lazy {
        peerConnectionFactory.makeAudioTrack(
            source = audioSource,
            trackId = "${TrackType.AUDIO.type}${ID_SEPARATOR}${peerConnectionFactory.sessionId}"
        )
    }

    override fun setMicMute(isMute: Boolean) {
        localAudioTrack.setEnabled(!isMute)
    }
    override fun setEnableSpeakerphone(enabled: Boolean) {
        audioController.setEnableSpeakerphone(enabled)
    }

    override fun addLocalAudioTrack(addTrack: (MediaStreamTrack) -> Unit) {
        audioController.switcherStart()
        addTrack(localAudioTrack)
    }

    override fun dispose() {
        audioController.switcherStop()
    }

    private fun buildAudioConstraints(): MediaConstraints {
        val mediaConstraints = MediaConstraints().apply {
            mandatory.addAll(
                listOf(
                    MediaConstraints.KeyValuePair("googEchoCancellation", "true"),
                    MediaConstraints.KeyValuePair("googAutoGainControl", "true"),
                    MediaConstraints.KeyValuePair("googHighpassFilter", "true"),
                    MediaConstraints.KeyValuePair("googNoiseSuppression", "true"),
                    MediaConstraints.KeyValuePair("googTypingNoiseDetection", "true")
                )
            )
        }

        return mediaConstraints.apply {
            with(optional) {
                add(MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"))
            }
        }
    }
}