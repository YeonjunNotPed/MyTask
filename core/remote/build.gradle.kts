import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.core.remote"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "BASE_URL", getProperty("BASE_URL"))
        buildConfigField("String", "WEBSOCKET_BASE_URL", getProperty("WEBSOCKET_BASE_URL"))
        buildConfigField("String", "CHAT_GPT_API_KEY", getProperty("CHAT_GPT_API_KEY"))
        buildConfigField("String", "GEMINI_API_KEY", getProperty("GEMINI_API_KEY"))
    }
}

dependencies {
    implementation(projects.core.modelData)
    implementation(libs.bundles.network)
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}