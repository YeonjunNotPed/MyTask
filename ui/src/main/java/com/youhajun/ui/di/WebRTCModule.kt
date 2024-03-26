package com.youhajun.ui.di

import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.managers.WebRtcAudioManager
import com.youhajun.ui.utils.webRtc.managers.WebRtcVideoManager
import com.youhajun.ui.utils.webRtc.peer.StreamPeerConnectionFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebRTCModule {

    @Binds
    @Singleton
    abstract fun bindPeerConnectionFactory(impl: StreamPeerConnectionFactory): WebRTCContract.PeerConnectionFactory

    @Binds
    @Singleton
    abstract fun bindVideoManager(impl: WebRtcVideoManager): WebRTCContract.VideoManager

    @Binds
    @Singleton
    abstract fun bindAudioManager(impl: WebRtcAudioManager): WebRTCContract.AudioManager
}