package com.youhajun.ui.di

import com.youhajun.ui.utils.audio.AudioController
import com.youhajun.ui.utils.audio.AudioControllerImpl
import com.youhajun.ui.utils.audio.AudioManagerAdapterImpl
import com.youhajun.ui.utils.audio.AudioSwitcherImpl
import com.youhajun.ui.utils.audio.bluetooth.BluetoothHeadsetManagerImpl
import com.youhajun.ui.utils.audio.wired.WiredHeadsetManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AudioModule {
    @Binds
    @ViewModelScoped
    abstract fun bindAudioController(impl: AudioControllerImpl): AudioController

    @Binds
    @ViewModelScoped
    abstract fun bindAudioManagerAdapter(impl: AudioManagerAdapterImpl): AudioController.AudioManagerAdapter

    @Binds
    @ViewModelScoped
    abstract fun bindAudioSwitcher(impl: AudioSwitcherImpl): AudioController.AudioSwitcher

    @Binds
    @ViewModelScoped
    abstract fun bindBluetoothHeadsetManager(impl: BluetoothHeadsetManagerImpl): AudioController.BluetoothHeadsetManager

    @Binds
    @ViewModelScoped
    abstract fun bindWiredHeadsetManager(impl: WiredHeadsetManagerImpl): AudioController.WiredHeadsetManager
}