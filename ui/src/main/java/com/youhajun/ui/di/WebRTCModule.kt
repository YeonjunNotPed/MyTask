package com.youhajun.ui.di

import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.managers.WebRtcAudioManager
import com.youhajun.ui.utils.webRtc.managers.WebRtcVideoManager
import com.youhajun.ui.utils.webRtc.peer.StreamPeerConnectionFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import org.webrtc.EglBase
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class WebRTCModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPeerConnectionFactory(impl: StreamPeerConnectionFactory): WebRTCContract.PeerConnectionFactory

    @Binds
    @ViewModelScoped
    abstract fun bindVideoManager(impl: WebRtcVideoManager): WebRTCContract.VideoManager

    @Binds
    @ViewModelScoped
    abstract fun bindAudioManager(impl: WebRtcAudioManager): WebRTCContract.AudioManager

    companion object {
        @Provides
        @ViewModelScoped
        fun providesEglBaseContext(): EglBase.Context {
            return EglBase.create().eglBaseContext
        }
    }
}