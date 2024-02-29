import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.youhajun.mytask"
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        applicationId = "com.youhajun.mytask"
        minSdk = Configs.MIN_SDK
        targetSdk = Configs.TARGET_SDK
        versionCode = Configs.VERSION_CODE
        versionName = Configs.VERSION_NAME

        buildConfigField("String", "KAKAO_NATIVE_KEY", getProperty("KAKAO_NATIVE_KEY"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(getProperty("STORE_FILE"))
            storePassword = getProperty("STORE_PASSWORD")
            keyAlias = getProperty("KEY_ALIAS")
            keyPassword = getProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":domain"))
    implementation(project(":data"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation("com.kakao.sdk:v2-user:2.19.0")
}

kapt {
    correctErrorTypes = true
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}