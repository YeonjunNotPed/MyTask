package com.youhajun.ui.utils.audio

import com.youhajun.ui.di.DefaultDispatcher
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.pow

@ViewModelScoped
class SoundUtil @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {
    companion object {
        private const val MAX_AMPLITUDE = 32767
        private const val MIN_THRESHOLD_AMPLITUDE = 100
    }

    private val audioLevelMutableList = mutableListOf<Float>()
    private val mutex = Mutex()

    suspend fun getAudioLevelList(audioBuffer: ShortArray, collectSize:Int): Result<List<Float>> = withContext(defaultDispatcher) {
        val audioLevel = calculateAudioLevel(audioBuffer)
        mutex.withLock {
            audioLevelMutableList.add(audioLevel)

            return@withContext when {
                audioLevelMutableList.size >= collectSize -> endAudioLevelCollect()
                else -> Result.failure(IllegalStateException("audioLevelList 수집 중"))
            }
        }
    }

    private fun endAudioLevelCollect(): Result<List<Float>> {
        val list = audioLevelMutableList.toList()
        audioLevelMutableList.clear()
        return Result.success(list)
    }

    private fun calculateAudioLevel(audioBuffer: ShortArray): Float {
        val amplitude = calculateMaxAmplitude(audioBuffer)
        val decibel = amplitudeToDecibel(amplitude)
        return if (decibel.isFinite()) decibelToLinear(decibel).doubleToUntilPoint(2) else 0f
    }

    private fun calculateMaxAmplitude(audioBuffer: ShortArray): Double {
        return audioBuffer.maxOrNull()?.toDouble() ?: 0.0
    }

    /**
     * MIN -90.3dB ~ MAX 0dB
     */
    private fun amplitudeToDecibel(amplitude: Double): Double {
        return if (amplitude < MIN_THRESHOLD_AMPLITUDE) Double.NEGATIVE_INFINITY
        else 20.0 * log10(amplitude / MAX_AMPLITUDE)
    }

    private fun decibelToLinear(dbValue: Double): Double {
        return 10.0.pow(dbValue / 20.0)
    }

    private fun Double.doubleToUntilPoint(pointIndex: Int):Float {
        return BigDecimal(this).setScale(pointIndex, RoundingMode.HALF_EVEN).toFloat()
    }
}