plugins {
    alias(libs.plugins.jetbrains.jvm)
}

dependencies {
    implementation(project(":core:model-data"))
    implementation(libs.kotlinx.collections.immutable)
}