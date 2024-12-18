plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.youhajun.core.datastore"
    compileSdk = Configs.COMPILE_SDK

    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }

    defaultConfig {
        minSdk = Configs.MIN_SDK
    }
}

dependencies {
    implementation(libs.bundles.datastore)
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
}