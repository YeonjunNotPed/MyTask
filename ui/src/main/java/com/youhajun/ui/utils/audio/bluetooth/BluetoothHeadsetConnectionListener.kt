package com.youhajun.ui.utils.audio.bluetooth

interface BluetoothHeadsetConnectionListener {
    fun onBluetoothHeadsetStateChanged(headsetName: String? = null)
    fun onBluetoothHeadsetActivationError()
}
