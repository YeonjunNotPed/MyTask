package com.youhajun.ui.utils.audio.bluetooth

import android.util.Log
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

/* Sometimes bluetooth sco not starting in some devices immediately
             so giving a delay and running it main thread */
@ViewModelScoped
class BluetoothScoLauncher @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val timeout: Long = TIMEOUT,
    private val interval: Long = INTERVAL,
) {
    companion object {
        private const val TAG = "BluetoothScoJob"
        private const val TIMEOUT = 10000L
        private const val INTERVAL = 500L
    }

    private var job: Job? = null

    fun launchBluetoothSco(
        scoTimeOutAction: suspend () -> Unit,
        scoAction: suspend () -> Unit
    ) {
        if(job?.isActive == true) return

        job = coroutineScope.launch {
            try {
                withTimeout(timeout) {
                    while (isActive) {
                        scoAction()
                        delay(interval)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                scoTimeOutAction()
            } finally {
                Log.d(TAG, "Bluetooth SCO job completed or cancelled")
            }
        }
    }

    fun cancelBluetoothSco() {
        job?.cancel()
        job = null
    }
}