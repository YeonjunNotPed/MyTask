plugins {

    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.domain"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.modelData)
}