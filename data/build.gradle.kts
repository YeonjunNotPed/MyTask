import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("kapt")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.android)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.youhajun.data"
    compileSdk = Configs.COMPILE_SDK

    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }

    defaultConfig {
        minSdk = Configs.MIN_SDK
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "../proguard-rules.pro",
                "../proguard_retrofit2.pro",
                "../proguard_gson.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":core:model-data"))
    implementation(project(":core:room"))
    implementation(project(":core:remote"))
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    implementation(libs.security.crypto)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.network)
}