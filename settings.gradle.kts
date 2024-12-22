pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "MyTask"
include(":ui")
include(":core:data")
include(":core:remote")
include(":core:room")
include(":core:datastore")
include(":core:model-ui")
include(":core:model-data")
include(":core:common")
include(":domain")
