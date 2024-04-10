package com.youhajun.ui.utils.audio

import android.media.AudioManager
import android.os.Build
import com.youhajun.ui.utils.audio.bluetooth.BluetoothHeadsetConnectionListener
import com.youhajun.ui.utils.audio.model.AudioDeviceInfoVo
import com.youhajun.ui.utils.audio.model.AudioDeviceType
import com.youhajun.ui.utils.audio.model.SwitcherStateType
import com.youhajun.ui.utils.audio.wired.WiredDeviceConnectionListener
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ViewModelScoped
class AudioSwitcherImpl @Inject constructor(
    private val audioManagerAdapter: AudioController.AudioManagerAdapter,
    private val bluetoothHeadsetManager: AudioController.BluetoothHeadsetManager,
    private val wiredHeadsetManager: AudioController.WiredHeadsetManager,
): AudioController.AudioSwitcher {

    private val preferredDeviceList:List<AudioDeviceType> by lazy {
        listOf(
            AudioDeviceType.BluetoothHeadset,
            AudioDeviceType.WiredHeadset,
            AudioDeviceType.Earpiece,
            AudioDeviceType.Speakerphone,
        )
    }

    private val _currentDeviceInfo: MutableStateFlow<AudioDeviceInfoVo> = MutableStateFlow(AudioDeviceInfoVo())
    override val currentDeviceInfo: StateFlow<AudioDeviceInfoVo> = _currentDeviceInfo.asStateFlow()
    private var switcherStateType: SwitcherStateType = SwitcherStateType.STOPPED
    private var userPreferSelectedDevice: AudioDeviceType? = null

    private val bluetoothDeviceConnectionListener = object : BluetoothHeadsetConnectionListener {
        override fun onBluetoothHeadsetStateChanged(headsetName: String?) {
            audioSynchronize()
        }

        override fun onBluetoothHeadsetActivationError() {
            if (userPreferSelectedDevice == AudioDeviceType.BluetoothHeadset) userPreferSelectedDevice = null
            audioSynchronize()
        }
    }

    private val wiredDeviceConnectionListener = object : WiredDeviceConnectionListener {
        override fun onDeviceConnected() {
            audioSynchronize()
        }

        override fun onDeviceDisconnected() {
            audioSynchronize()
        }
    }

    override suspend fun start() {
        switcherStateType = SwitcherStateType.STARTED
        collectDeviceInfo()
        bluetoothHeadsetManager.registerHeadsetConnect(bluetoothDeviceConnectionListener)
        wiredHeadsetManager.registerHeadsetConnect(wiredDeviceConnectionListener)
        audioSynchronize()
    }
    override fun stop() {
        if(switcherStateType == SwitcherStateType.AUDIO_WORKING) deactivateDevice()
        switcherStateType = SwitcherStateType.STOPPED
        bluetoothHeadsetManager.unregisterHeadsetConnect()
        wiredHeadsetManager.unregisterHeadsetConnect()
    }

    override fun onUserPreferSelectDevice(userPreferAudioDevice: AudioDeviceType?) {
        val currentDevice = currentDeviceInfo.value.currentAudioDevice
        if (currentDevice != userPreferAudioDevice) {
            userPreferSelectedDevice = userPreferAudioDevice
            audioSynchronize()
        }
    }

    private fun audioSynchronize() {
        val availableList = getAvailableAudioDevices()
        if (!isUserPreferSelectedDeviceAvailable(availableList)) {
            userPreferSelectedDevice = null
        }

        val currentSelectedDevice = getCurrentSelectedDevice(availableList)

        updateAudioDeviceInfo(
            newCurrentDevice = currentSelectedDevice,
            newAvailableDeviceList = availableList
        )
    }

    private fun getCurrentSelectedDevice(availableDeviceList: List<AudioDeviceType>): AudioDeviceType? {
        return userPreferSelectedDevice ?: run {
            availableDeviceList.firstOrNull()?.takeUnless {
                it == AudioDeviceType.BluetoothHeadset && bluetoothHeadsetManager.hasActivationError()
            } ?: availableDeviceList.getOrNull(1)
        }
    }

    private fun getAvailableAudioDevices(): List<AudioDeviceType> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            audioManagerAdapter.getAvailableAudioDevices()
        } else {
            preferredDeviceList.filter { device ->
                when (device) {
                    AudioDeviceType.BluetoothHeadset -> bluetoothHeadsetManager.isBluetoothHeadsetAvailable()
                    AudioDeviceType.WiredHeadset -> wiredHeadsetManager.isWiredHeadsetAvailable()
                    AudioDeviceType.Earpiece -> audioManagerAdapter.hasEarpiece() && !wiredHeadsetManager.isWiredHeadsetAvailable()
                    AudioDeviceType.Speakerphone -> audioManagerAdapter.hasSpeakerphone()
                }
            }
        }
    }

    private fun isUserPreferSelectedDeviceAvailable(availableDeviceList: List<AudioDeviceType>): Boolean {
        return userPreferSelectedDevice?.let { userPreferDevice ->
            availableDeviceList.contains(userPreferDevice)
        } ?: false
    }

    private fun updateAudioDeviceInfo(
        newCurrentDevice: AudioDeviceType? = null,
        newAvailableDeviceList: List<AudioDeviceType>? = null
    ) {
        _currentDeviceInfo.update {
            val currentDevice = newCurrentDevice ?: it.currentAudioDevice
            val availableDeviceList = newAvailableDeviceList ?: it.availableDeviceList

            it.copy(
                currentAudioDevice = currentDevice,
                availableDeviceList = availableDeviceList
            )
        }
    }

    private suspend fun collectDeviceInfo() {
        currentDeviceInfo.collect {
            if(switcherStateType != SwitcherStateType.STOPPED) {
                audioWorking(it.currentAudioDevice)
            }
        }
    }

    private fun audioWorking(currentDeviceType: AudioDeviceType?) {
        currentDeviceType ?: return

        switcherStateType = SwitcherStateType.AUDIO_WORKING

        if(switcherStateType == SwitcherStateType.STARTED) {
            audioManagerAdapter.cachePreviousAudioState()

            // Always set mute to false for WebRTC
            audioManagerAdapter.setMicMute(false)
            audioManagerAdapter.setAudioFocus()
        }

        activateDevice(currentDeviceType)
    }

    private fun activateDevice(audioDevice: AudioDeviceType) {
        when (audioDevice) {
            AudioDeviceType.BluetoothHeadset -> {
                audioManagerAdapter.setAudioMode(AudioManager.MODE_IN_COMMUNICATION)
                audioManagerAdapter.setEnableSpeakerphone(false)
                bluetoothHeadsetManager.activateHeadset()
            }
            AudioDeviceType.Earpiece,
            AudioDeviceType.WiredHeadset -> {
                audioManagerAdapter.setAudioMode(AudioManager.MODE_IN_COMMUNICATION)
                audioManagerAdapter.setEnableSpeakerphone(false)
                bluetoothHeadsetManager.deactivateHeadset()
            }
            AudioDeviceType.Speakerphone -> {
                audioManagerAdapter.setAudioMode(AudioManager.MODE_NORMAL)
                audioManagerAdapter.setEnableSpeakerphone(true)
                bluetoothHeadsetManager.deactivateHeadset()
            }
        }
    }

    private fun deactivateDevice() {
        if(switcherStateType != SwitcherStateType.AUDIO_WORKING) return

        switcherStateType = SwitcherStateType.STARTED
        bluetoothHeadsetManager.deactivateHeadset()
        audioManagerAdapter.restorePreviousAudioState()
    }
}
