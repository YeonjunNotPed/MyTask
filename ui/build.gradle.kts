import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("kapt")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.android)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.youhajun.ui"
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        minSdk = Configs.MIN_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["KAKAO_NATIVE_KEY"] = getProperty("KAKAO_NATIVE_KEY")
        buildConfigField("String", "GOOGLE_API_SERVER_ID", getProperty("GOOGLE_API_SERVER_ID"))
        buildConfigField("String", "STUN_SERVER_URL1", getProperty("STUN_SERVER_URL1"))
        buildConfigField("String", "STUN_SERVER_URL2", getProperty("STUN_SERVER_URL2"))
        buildConfigField("String", "STUN_SERVER_URL3", getProperty("STUN_SERVER_URL3"))
        buildConfigField("String", "STUN_SERVER_URL4", getProperty("STUN_SERVER_URL4"))
        buildConfigField("String", "STUN_SERVER_URL5", getProperty("STUN_SERVER_URL5"))
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
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

    testImplementation(libs.bundles.test)
    debugImplementation(libs.bundles.debug)
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}

kapt {
    correctErrorTypes = true
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}