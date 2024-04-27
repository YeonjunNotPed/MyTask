plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.youhajun.domain"
    compileSdk = Configs.COMPILE_SDK

    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }

    defaultConfig {
        minSdk = Configs.MIN_SDK
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":core:model-data"))
    implementation(project(":core:common"))
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
}