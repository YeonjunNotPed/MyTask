plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.youhajun.android.room)
}

android {
    namespace = "com.youhajun.core.room"
}

dependencies {
    implementation(projects.core.modelData)
}