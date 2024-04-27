pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    buildscript {
        repositories {
            mavenCentral()
            maven {
                url = uri("https://storage.googleapis.com/r8-releases/raw")
            }
        }
        dependencies {
            classpath("com.android.tools:r8:8.3.37")
        }
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

rootProject.name = "MyTask"
include(":app")
include(":data")
include(":ui")
include(":core:remote")
include(":core:room")
include(":core:datastore")
include(":core:model-ui")
include(":core:model-data")
include(":core:common")
include(":domain")
