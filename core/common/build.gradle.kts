plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.youhajun.core.common"
    compileSdk = Configs.COMPILE_SDK

    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }

    defaultConfig {
        minSdk = Configs.MIN_SDK
    }
}

dependencies {
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
}