package com.youhajun.ui

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import io.getstream.log.Priority
import io.getstream.log.StreamLog
import io.getstream.log.kotlin.KotlinStreamLogger


@HiltAndroidApp
class MyTaskApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        initLogger()
    }

    private fun initLogger() {
        StreamLog.install(KotlinStreamLogger())

        StreamLog.setValidator { priority, _ ->
            priority.level >= Priority.DEBUG.level
        }
    }
}