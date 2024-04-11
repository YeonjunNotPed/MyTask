package com.youhajun.ui.utils.audio

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.youhajun.ui.utils.audio.model.AudioDeviceType
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AudioManagerAdapterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioFocusRequest: AudioFocusRequestWrapper,
) : AudioController.AudioManagerAdapter {

    private var audioRequest: AudioFocusRequest? = null
    private var savedAudioMode = 0
    private var savedIsMicrophoneMuted = false
    private var savedSpeakerphoneEnabled = false

    private val audioManager by lazy {
        context.getSystemService(AudioManager::class.java)
    }

    private val audioFocusChangeListener by lazy {
        AudioManager.OnAudioFocusChangeListener { focusChange ->

        }
    }
    override fun hasEarpiece(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
    }

    override fun hasSpeakerphone(): Boolean {
        return if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
            true
        } else {
            val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            devices.any { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
        }
    }

    override fun setMicMute(mute: Boolean) {
        audioManager.isMicrophoneMute = mute
    }

    override fun setEnableSpeakerphone(enable: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (enable) setCommunicationDevice(listOf(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER))
            else clearCommunicationDevice()
        }else {
            audioManager.isSpeakerphoneOn = enable
        }
    }

    override fun setEnableBluetoothSco(enable: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (enable) setCommunicationDevice(listOf(AudioDeviceInfo.TYPE_BLUETOOTH_SCO))
            else clearCommunicationDevice()
        }else {
            audioManager.isSpeakerphoneOn = enable
            if (enable) audioManager.startBluetoothSco() else audioManager.stopBluetoothSco()
        }
    }

    override fun setAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioRequest = audioFocusRequest.buildRequest(audioFocusChangeListener)
            audioRequest?.let {
                audioManager.requestAudioFocus(it)
            } ?: AudioManager.AUDIOFOCUS_REQUEST_FAILED
        } else {
            audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
        }
    }

    override fun setAudioMode(audioMode: Int) {
        audioManager.mode = audioMode
    }

    override fun cachePreviousAudioState() {
        savedAudioMode = audioManager.mode
        savedIsMicrophoneMuted = audioManager.isMicrophoneMute
        savedSpeakerphoneEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            audioManager.communicationDevice?.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER
        }else {
            audioManager.isSpeakerphoneOn
        }
    }

    override fun restorePreviousAudioState() {
        setAudioMode(savedAudioMode)
        setMicMute(savedIsMicrophoneMuted)
        setEnableSpeakerphone(savedSpeakerphoneEnabled)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioRequest?.let { audioManager.abandonAudioFocusRequest(it) }
        } else {
            audioManager.abandonAudioFocus(audioFocusChangeListener)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun getAvailableAudioDevices(): List<AudioDeviceType> {
        return audioManager.availableCommunicationDevices.mapNotNull {
            AudioDeviceType.from(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setCommunicationDevice(typeList: List<Int>) {
        val device = getAudioDevices().firstOrNull { typeList.contains(it.type) }
        device?.let { audioManager.setCommunicationDevice(it) }
    }

    private fun getAudioDevices(): List<AudioDeviceInfo> {
        return audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).toList()
    }
    @RequiresApi(Build.VERSION_CODES.S)
    private fun clearCommunicationDevice() {
        audioManager.clearCommunicationDevice()
    }
}

