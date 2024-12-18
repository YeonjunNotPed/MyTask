plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.youhajun.core.room"
    compileSdk = Configs.COMPILE_SDK

    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }

    defaultConfig {
        minSdk = Configs.MIN_SDK
    }
}

dependencies {
    implementation(project(":core:model-data"))
    implementation(libs.bundles.room)
    implementation(libs.hilt)
    kapt(libs.room.compiler)
    kapt(libs.hilt.compiler)
}