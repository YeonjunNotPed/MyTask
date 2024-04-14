package com.youhajun.ui.utils.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.youhajun.ui.di.DefaultDispatcher
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class AudioRecorder @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val soundUtil: SoundUtil
) {
    companion object {
        private const val SAMPLE_RATE = 48000
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        private const val AUDIO_SOURCE = MediaRecorder.AudioSource.MIC
        private const val AUDIO_LEVEL_COLLECT_SIZE = 5
        private const val AUDIO_LEVEL_COLLECT_INTERVAL = 50L
    }

    private val bufferSizeInBytes: Int = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
    private val audioRecord: AudioRecord = AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSizeInBytes)
    private val scope = CoroutineScope(defaultDispatcher)
    private var job: Job? = null
    private val _audioLevelListSharedFlow: MutableSharedFlow<List<Float>> = MutableSharedFlow()
    val audioLevelListSharedFlow: SharedFlow<List<Float>> = _audioLevelListSharedFlow.asSharedFlow()

    fun startCapturing() {
        if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
            throw IllegalStateException("AudioRecord initialization failed.")
        }
        audioRecord.startRecording()

        job = scope.launch {
            while (isActive) {
                val audioBuffer = ShortArray(bufferSizeInBytes / 2)
                val pcmData = audioRecord.read(audioBuffer, 0, audioBuffer.size)
                if (pcmData < 0) break

                soundUtil.getAudioLevelList(audioBuffer, AUDIO_LEVEL_COLLECT_SIZE).onSuccess {
                    _audioLevelListSharedFlow.emit(it)
                }.onFailure {
                    delay(AUDIO_LEVEL_COLLECT_INTERVAL)
                }
            }
        }
    }

    fun stopCapturing() {
        job?.cancel()
        job = null
        audioRecord.stop()
        audioRecord.release()
    }
}