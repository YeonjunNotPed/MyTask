import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.android)
    alias(libs.plugins.hilt)
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
        signingConfig = signingConfigs.getByName("debug")
    }

//    signingConfigs {
//        create("release") {
//            storeFile = file(getProperty("STORE_FILE"))
//            storePassword = getProperty("STORE_PASSWORD")
//            keyAlias = getProperty("KEY_ALIAS")
//            keyPassword = getProperty("KEY_PASSWORD")
//        }
//    }

    buildTypes {
        getByName("release") {
//            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "../proguard-rules.pro",
                "../proguard_retrofit2.pro",
                "../proguard_gson.pro",
            )
        }
    }

    kotlinOptions {
        jvmTarget = Configs.JVM_TARGET
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    implementation(project(":data"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model-data"))
    implementation(project(":core:model-ui"))
    implementation(project(":core:remote"))
    implementation(project(":core:room"))

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    implementation(libs.kakao.login)

    implementation(libs.stream.log)
}

kapt {
    correctErrorTypes = true
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir,providers).getProperty(propertyKey)
}