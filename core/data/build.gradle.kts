plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.core.data"
}

dependencies {
    implementation(projects.core.modelData)
    implementation(projects.core.room)
    implementation(projects.core.remote)
    implementation(projects.core.datastore)
    implementation(projects.core.common)

    implementation(libs.security.crypto)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.network)
}