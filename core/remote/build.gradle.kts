import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.youhajun.core.remote"
    compileSdk = Configs.COMPILE_SDK

    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        minSdk = Configs.MIN_SDK
        buildConfigField("String", "BASE_URL", getProperty("BASE_URL"))
        buildConfigField("String", "WEBSOCKET_BASE_URL", getProperty("WEBSOCKET_BASE_URL"))
        buildConfigField("String", "CHAT_GPT_API_KEY", getProperty("CHAT_GPT_API_KEY"))
        buildConfigField("String", "GEMINI_API_KEY", getProperty("GEMINI_API_KEY"))
    }
}

dependencies {
    implementation(project(":core:model-data"))
    implementation(libs.bundles.network)
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}