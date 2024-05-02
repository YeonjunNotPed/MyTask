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

package com.youhajun.ui.utils.webRtc.peer

import android.content.Context
import android.os.Build
import com.youhajun.ui.BuildConfig
import com.youhajun.ui.utils.webRtc.Loggers
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.model_ui.types.webrtc.StreamPeerType
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.EglBase
import org.webrtc.Logging
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SoftwareVideoDecoderFactory
import org.webrtc.SoftwareVideoEncoderFactory
import org.webrtc.VideoSource
import org.webrtc.VideoTrack
import org.webrtc.audio.AudioDeviceModule
import org.webrtc.audio.JavaAudioDeviceModule
import java.util.UUID
import javax.inject.Inject


@ViewModelScoped
class StreamPeerConnectionFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eglBaseContext: EglBase.Context
) : WebRTCContract.PeerConnectionFactory {

    override val sessionId: String = UUID.randomUUID().toString()

    private val videoDecoderFactory by lazy {
//        DefaultVideoDecoderFactory(eglBaseContext)
        SoftwareVideoDecoderFactory()
    }

    private val videoEncoderFactory by lazy {
        SoftwareVideoEncoderFactory()
//        val hardwareEncoder = HardwareVideoEncoderFactory(eglBaseContext, true, true)
//        SimulcastVideoEncoderFactory(hardwareEncoder, SoftwareVideoEncoderFactory())
    }

    private val mediaConstraints = MediaConstraints().apply {
        mandatory.addAll(
            listOf(
                MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"),
                MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true")
            )
        )
    }

    private val iceServerUrls = listOf(
        BuildConfig.STUN_SERVER_URL1,
        BuildConfig.STUN_SERVER_URL2,
        BuildConfig.STUN_SERVER_URL3,
        BuildConfig.STUN_SERVER_URL4,
        BuildConfig.STUN_SERVER_URL5
    )

    private val iceServers: List<PeerConnection.IceServer> by lazy {
        listOf(PeerConnection.IceServer.builder(iceServerUrls).createIceServer())
    }

    private val rtcConfig: PeerConnection.RTCConfiguration = PeerConnection.RTCConfiguration(iceServers).apply {
        sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
    }

    private val factory: PeerConnectionFactory by lazy {
        initializePeerConnectionFactory()
        createPeerConnectionFactory()
    }

    private fun initializePeerConnectionFactory() {
        val options = PeerConnectionFactory.InitializationOptions.builder(context)
            .setInjectableLogger(Loggers.webRTCLog(), Logging.Severity.LS_VERBOSE)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
    }

    private fun createPeerConnectionFactory(): PeerConnectionFactory =
        PeerConnectionFactory.builder()
            .setVideoDecoderFactory(videoDecoderFactory)
            .setVideoEncoderFactory(videoEncoderFactory)
            .setAudioDeviceModule(createAudioDeviceModule())
            .createPeerConnectionFactory()

    private fun createAudioDeviceModule(): AudioDeviceModule {
        val isAssistedHardwareAcousticEchoCanceler = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        val isAssistedHardwareNoiseSuppressor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        return JavaAudioDeviceModule.builder(context)
            .setUseHardwareAcousticEchoCanceler(isAssistedHardwareAcousticEchoCanceler)
            .setUseHardwareNoiseSuppressor(isAssistedHardwareNoiseSuppressor)
            .setAudioRecordErrorCallback(Loggers.Audio.audioRecordErrorLog())
            .setAudioTrackErrorCallback(Loggers.Audio.audioTrackErrorLog())
            .setAudioRecordStateCallback(Loggers.Audio.audioRecordStateLog())
            .setAudioTrackStateCallback(Loggers.Audio.audioTrackStateLog())
            .createAudioDeviceModule().also {
                it.setMicrophoneMute(false)
                it.setSpeakerMute(false)
            }
    }

    override fun makeVideoSource(isScreencast: Boolean): VideoSource =
        factory.createVideoSource(isScreencast)

    override fun makeVideoTrack(
        source: VideoSource,
        trackId: String
    ): VideoTrack = factory.createVideoTrack(trackId, source)

    override fun makeAudioSource(constraints: MediaConstraints): AudioSource = factory.createAudioSource(constraints)

    override fun makeAudioTrack(
        source: AudioSource,
        trackId: String
    ): AudioTrack = factory.createAudioTrack(trackId, source)

    override fun makePeerConnection(
        type: StreamPeerType,
        peerConnectionListener: WebRTCContract.PeerConnectionListener
    ): StreamPeerConnection {
        val peerConnectionObserver = PeerConnectionObserver(
            type = type,
            peerConnectionListener = peerConnectionListener,
        )
        val peerConnection = makePeerConnectionInternal(
            configuration = rtcConfig,
            observer = peerConnectionObserver
        )
        return StreamPeerConnection(
            type = type,
            mediaConstraints = mediaConstraints,
            connection = peerConnection
        )
    }

    private fun makePeerConnectionInternal(
        configuration: PeerConnection.RTCConfiguration,
        observer: PeerConnection.Observer?
    ): PeerConnection {
        return requireNotNull(
            factory.createPeerConnection(
                configuration,
                observer
            )
        )
    }
}