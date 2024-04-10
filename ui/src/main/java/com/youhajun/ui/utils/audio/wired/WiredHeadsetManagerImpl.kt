package com.youhajun.ui.utils.audio.wired

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.youhajun.ui.utils.audio.AudioController
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

private const val TAG = "WiredHeadsetReceiver"
internal const val STATE_UNPLUGGED = 0
internal const val STATE_PLUGGED = 1
internal const val INTENT_STATE = "state"
internal const val INTENT_NAME = "name"

@ViewModelScoped
class WiredHeadsetManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AudioController.WiredHeadsetManager, BroadcastReceiver() {

    private var deviceListener: WiredDeviceConnectionListener? = null
    private var isConnectedWired:Boolean = false
    private var isRegistered = false

    override fun onReceive(context: Context, intent: Intent) {
        intent.getIntExtra(INTENT_STATE, STATE_UNPLUGGED).let { state ->
            if (state == STATE_PLUGGED) {
                intent.getStringExtra(INTENT_NAME).let { name ->
                    Log.d(TAG, "Wired headset device ${name ?: ""} connected")
                }
                isConnectedWired = true
                deviceListener?.onDeviceConnected()
            } else {
                intent.getStringExtra(INTENT_NAME).let { name ->
                    Log.d(TAG, "Wired headset device ${name ?: ""} disconnected")
                }
                isConnectedWired = false
                deviceListener?.onDeviceDisconnected()
            }
        }
    }

    override fun registerHeadsetConnect(headsetListener: WiredDeviceConnectionListener) {
        isRegistered = true
        this.deviceListener = headsetListener
        context.registerReceiver(this, IntentFilter(Intent.ACTION_HEADSET_PLUG))
    }

    override fun unregisterHeadsetConnect() {
        if(isRegistered.not()) return

        isRegistered = false
        deviceListener = null
        context.unregisterReceiver(this)
    }

    override fun isWiredHeadsetAvailable(): Boolean {
        return isConnectedWired
    }
}
