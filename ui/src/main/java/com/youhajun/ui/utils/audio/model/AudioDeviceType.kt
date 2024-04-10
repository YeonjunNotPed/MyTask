package com.youhajun.ui.utils.audio.model

import android.media.AudioDeviceInfo

enum class AudioDeviceType(val type: String) {
    BluetoothHeadset("Bluetooth"),
    WiredHeadset("Wired Headset"),
    Earpiece("Earpiece"),
    Speakerphone("Speakerphone");

    companion object {
        fun from(info: AudioDeviceInfo): AudioDeviceType? {
            return when (info.type) {
                AudioDeviceInfo.TYPE_BLUETOOTH_SCO -> BluetoothHeadset
                AudioDeviceInfo.TYPE_WIRED_HEADSET -> WiredHeadset
                AudioDeviceInfo.TYPE_BUILTIN_EARPIECE -> Earpiece
                AudioDeviceInfo.TYPE_BUILTIN_SPEAKER -> Speakerphone
                else -> null
            }
        }
    }
}
