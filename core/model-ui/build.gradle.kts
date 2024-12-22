plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
}

android {
    namespace = "com.youhajun.core.model.ui"
}

dependencies {
    implementation(projects.core.modelData)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.bundles.webrtc)
    implementation(libs.androidx.annotation.jvm)
    implementation(libs.bundles.compose.libs)
}