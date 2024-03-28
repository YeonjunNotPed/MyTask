import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("kapt")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.youhajun.data"
    compileSdk = Configs.COMPILE_SDK

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        minSdk = Configs.MIN_SDK
        buildConfigField("String", "BASE_URL", getProperty("BASE_URL"))
        buildConfigField("String", "WEBSOCKET_BASE_URL", getProperty("WEBSOCKET_BASE_URL"))
        buildConfigField("String", "CHAT_GPT_API_KEY", getProperty("CHAT_GPT_API_KEY"))
        buildConfigField("String", "GEMINI_API_KEY", getProperty("GEMINI_API_KEY"))
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        getByName("release") {
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
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")


    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.49")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")

    implementation("androidx.security:security-crypto:1.0.0")

    implementation("io.getstream:stream-webrtc-android:1.0.3")
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}