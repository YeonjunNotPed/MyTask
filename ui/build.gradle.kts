import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.youhajun.ui"
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        applicationId = "com.youhajun.mytask"
        minSdk = Configs.MIN_SDK
        targetSdk = Configs.TARGET_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        versionCode = Configs.VERSION_CODE
        versionName = Configs.VERSION_NAME

        manifestPlaceholders["KAKAO_NATIVE_KEY"] = getProperty("KAKAO_NATIVE_KEY")
        buildConfigField("String", "GOOGLE_API_SERVER_ID", getProperty("GOOGLE_API_SERVER_ID"))
        buildConfigField("String", "STUN_SERVER_URL1", getProperty("STUN_SERVER_URL1"))
        buildConfigField("String", "STUN_SERVER_URL2", getProperty("STUN_SERVER_URL2"))
        buildConfigField("String", "STUN_SERVER_URL3", getProperty("STUN_SERVER_URL3"))
        buildConfigField("String", "STUN_SERVER_URL4", getProperty("STUN_SERVER_URL4"))
        buildConfigField("String", "STUN_SERVER_URL5", getProperty("STUN_SERVER_URL5"))
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

        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_compiler"
        )
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_compiler"
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Configs.COMPOSE_VERSION
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core:common"))
    implementation(project(":core:model-ui"))
    implementation(project(":core:model-data"))

    implementation(libs.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.compose.libs)
    implementation(libs.bundles.orbit)
    implementation(libs.bundles.webrtc)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    implementation(libs.kakao.login)

    implementation(libs.play.services.auth)

    implementation(libs.billing.ktx)

    implementation(libs.stream.log)

    implementation(libs.kotlinx.collections.immutable)

//    testImplementation(libs.bundles.test)
//    debugImplementation(libs.bundles.debug)
//    androidTestImplementation(libs.bundles.android.test)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
}

kapt {
    correctErrorTypes = true
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}