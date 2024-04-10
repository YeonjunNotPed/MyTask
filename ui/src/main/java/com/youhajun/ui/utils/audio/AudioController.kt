package com.youhajun.ui.utils.audio

import android.os.Build
import androidx.annotation.RequiresApi
import com.youhajun.ui.utils.audio.bluetooth.BluetoothHeadsetConnectionListener
import com.youhajun.ui.utils.audio.model.AudioDeviceInfoVo
import com.youhajun.ui.utils.audio.model.AudioDeviceType
import com.youhajun.ui.utils.audio.model.BluetoothDeviceInfoVo
import com.youhajun.ui.utils.audio.wired.WiredDeviceConnectionListener
import kotlinx.coroutines.flow.StateFlow

interface AudioController {

  fun switcherStart()
  fun switcherStop()
  fun setEnableSpeakerphone(enable: Boolean)

  interface BluetoothHeadsetManager {
    fun registerHeadsetConnect(bluetoothHeadsetConnectionListener: BluetoothHeadsetConnectionListener)
    fun unregisterHeadsetConnect()
    fun activateHeadset()
    fun deactivateHeadset()
    fun hasActivationError(): Boolean
    fun getCurrentHeadsetInfo(): BluetoothDeviceInfoVo?
    fun isBluetoothHeadsetAvailable(): Boolean
  }

  interface WiredHeadsetManager {
    fun registerHeadsetConnect(headsetListener: WiredDeviceConnectionListener)
    fun unregisterHeadsetConnect()
    fun isWiredHeadsetAvailable(): Boolean
  }

  interface AudioSwitcher {
    val currentDeviceInfo: StateFlow<AudioDeviceInfoVo>
    suspend fun start()
    fun stop()
    fun onUserPreferSelectDevice(userPreferAudioDevice: AudioDeviceType?)
  }

  interface AudioManagerAdapter {
    fun setMicMute(mute: Boolean)
    fun setEnableBluetoothSco(enable: Boolean)
    fun setEnableSpeakerphone(enable: Boolean)
    fun setAudioMode(audioMode: Int)
    fun setAudioFocus()
    fun hasEarpiece(): Boolean
    fun hasSpeakerphone(): Boolean
    fun cachePreviousAudioState()
    fun restorePreviousAudioState()
    @RequiresApi(Build.VERSION_CODES.S)
    fun getAvailableAudioDevices(): List<AudioDeviceType>
  }
}
