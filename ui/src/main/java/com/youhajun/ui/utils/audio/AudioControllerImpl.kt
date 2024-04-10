package com.youhajun.ui.utils.audio

import com.youhajun.ui.di.DefaultDispatcher
import com.youhajun.ui.utils.audio.model.AudioDeviceType
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class AudioControllerImpl @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val audioSwitcher: AudioController.AudioSwitcher,
) : AudioController {

    private var audioSwitchJob: Job? = null
    private val audioSwitchScope: CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    override fun switcherStart() {
        audioSwitchJob = audioSwitchScope.launch {
            launch { onCollectAudioDeviceInfo() }
            launch { audioSwitcher.start() }
        }
    }

    override fun switcherStop() {
        audioSwitcher.stop()
        audioSwitchJob?.cancel()
        audioSwitchJob = null
    }

    override fun setEnableSpeakerphone(enable: Boolean) {
        val device = if(enable) AudioDeviceType.Speakerphone else null
        audioSwitcher.onUserPreferSelectDevice(device)
    }

    private suspend fun onCollectAudioDeviceInfo() {
        audioSwitcher.currentDeviceInfo.collect { audioDeviceInfo ->

        }
    }
}
