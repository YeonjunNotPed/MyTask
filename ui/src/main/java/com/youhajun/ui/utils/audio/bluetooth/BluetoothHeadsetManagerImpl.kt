package com.youhajun.ui.utils.audio.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.youhajun.common.DefaultDispatcher
import com.youhajun.ui.utils.audio.AudioController
import com.youhajun.ui.utils.audio.model.BluetoothDeviceInfoVo
import com.youhajun.ui.utils.audio.model.BluetoothHeadsetState
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ViewModelScoped
class BluetoothHeadsetManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val audioManagerAdapter: AudioController.AudioManagerAdapter,
) : AudioController.BluetoothHeadsetManager, BluetoothProfile.ServiceListener {

    private var scope: CoroutineScope = CoroutineScope(defaultDispatcher)

    private val startBluetoothScoLauncher by lazy {
        BluetoothScoLauncher(scope)
    }

    private val stopBluetoothScoLauncher by lazy {
        BluetoothScoLauncher(scope)
    }

    private val bluetoothManager: BluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        bluetoothManager.adapter
    }

    private var headsetListener: BluetoothHeadsetConnectionListener? = null
    private var headsetInfoVo: BluetoothDeviceInfoVo? = null
    private var headsetProxy: BluetoothHeadset? = null
    private var headsetState: BluetoothHeadsetState = BluetoothHeadsetState.Disconnected

    private var isRegistered = false

    override fun onServiceConnected(profile: Int, bluetoothProfile: BluetoothProfile) {
        startBluetoothScoLauncher.cancelBluetoothSco()
        val headset = bluetoothProfile as BluetoothHeadset
        headsetState = BluetoothHeadsetState.Connected
        headsetProxy = headset
        updateHeadsetInfo(headset)
    }

    override fun onServiceDisconnected(p0: Int) {
        stopBluetoothScoLauncher.cancelBluetoothSco()
        headsetState = BluetoothHeadsetState.Disconnected
        headsetProxy = null
        headsetInfoVo = null
    }

    override fun registerHeadsetConnect(bluetoothHeadsetConnectionListener: BluetoothHeadsetConnectionListener) {
        isRegistered = true
        headsetListener = bluetoothHeadsetConnectionListener
        registerBluetoothProfileProxy()
    }

    override fun unregisterHeadsetConnect() {
        if(isRegistered.not()) return

        isRegistered = false
        headsetListener = null
        unregisterBluetoothProfileProxy()
        scope.cancel()
    }

    override fun activateHeadset() {
        if (!hasPermissions()) return

        startBluetoothSco()
    }

    override fun deactivateHeadset() {
        stopBluetoothSco()
    }

    override fun hasActivationError(): Boolean {
        if (!hasPermissions()) return false

        return headsetState == BluetoothHeadsetState.AudioActivationError
    }

    override fun getCurrentHeadsetInfo(): BluetoothDeviceInfoVo? {
        return headsetInfoVo
    }

    override fun isBluetoothHeadsetAvailable(): Boolean = headsetState == BluetoothHeadsetState.Connected || BluetoothHeadsetState.AudioActivated == headsetState

    private fun startBluetoothSco() {
        if(headsetState != BluetoothHeadsetState.Connected && headsetState != BluetoothHeadsetState.AudioActivationError) return

        startBluetoothScoLauncher.launchBluetoothSco(
            scoAction = {
                audioManagerAdapter.setEnableBluetoothSco(true)
                headsetState = BluetoothHeadsetState.AudioActivated
            },
            scoTimeOutAction = {
                headsetState = BluetoothHeadsetState.AudioActivationError
                headsetListener?.onBluetoothHeadsetActivationError()
            }
        )
    }

    private fun stopBluetoothSco() {
        if (headsetState != BluetoothHeadsetState.AudioActivated) return

        stopBluetoothScoLauncher.launchBluetoothSco(
            scoAction = {
                audioManagerAdapter.setEnableBluetoothSco(false)
                headsetState = BluetoothHeadsetState.Connected
            },
            scoTimeOutAction = {
                headsetState = BluetoothHeadsetState.AudioActivationError
            }
        )
    }

    private fun updateHeadsetInfo(headset: BluetoothHeadset) {
        val connectedDevices = headset.connectedDevices
        val device = connectedDevices.firstOrNull { headset.isAudioConnected(it) }
        headsetInfoVo = device?.let { BluetoothDeviceInfoVo(it) }
    }

    private fun registerBluetoothProfileProxy() {
        bluetoothAdapter?.getProfileProxy(context, this, BluetoothProfile.HEADSET)
    }

    private fun unregisterBluetoothProfileProxy() {
        bluetoothAdapter?.closeProfileProxy(BluetoothProfile.HEADSET, headsetProxy)
    }

    private fun hasPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH
        ) == PackageManager.PERMISSION_GRANTED
    }
}