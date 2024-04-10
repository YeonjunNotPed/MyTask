package com.youhajun.ui.utils.audio.model

import android.bluetooth.BluetoothDevice

data class BluetoothDeviceInfoVo(
    val device: BluetoothDevice,
    val name: String = device.name ?: DEFAULT_DEVICE_NAME,
    val deviceClass: Int? = device.bluetoothClass?.deviceClass,
) {
    companion object {
        const val DEFAULT_DEVICE_NAME = "Bluetooth"
    }
}
