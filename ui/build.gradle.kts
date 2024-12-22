import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.youhajun.android.application)
    alias(libs.plugins.youhajun.android.application.compose)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.ui"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {

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
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.modelUi)
    implementation(projects.core.modelData)

    implementation(libs.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.compose.libs)
    implementation(libs.bundles.orbit)
    implementation(libs.bundles.webrtc)

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

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}