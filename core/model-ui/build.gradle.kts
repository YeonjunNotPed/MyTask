plugins {
    kotlin("kapt")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.android)
}

android {
    namespace = "com.youhajun.core.model.ui"
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        minSdk = Configs.MIN_SDK
    }

    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {
    implementation(project(":core:model-data"))
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.annotation.jvm)
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.compose.libs)
    implementation(libs.bundles.webrtc)
}