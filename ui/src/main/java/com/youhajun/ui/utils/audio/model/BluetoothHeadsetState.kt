package com.youhajun.ui.utils.audio.model

sealed class BluetoothHeadsetState {
    object Connected : BluetoothHeadsetState()
    object Disconnected : BluetoothHeadsetState()
    object AudioActivationError : BluetoothHeadsetState()
    object AudioActivated : BluetoothHeadsetState()
}