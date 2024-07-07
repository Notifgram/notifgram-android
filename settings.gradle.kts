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
        maven("https://jitpack.io")
    }
}

rootProject.name = "notifgram"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:common")
include(":core:data-file")
include(":core:data-datastore")
include(":core:data-local")
include(":core:data-remote")
include(":core:data-repository")
include(":core:domain")
include(":core:presentation-core")
include(":presentation-channel")
include(":presentation-home")
include(":presentation-post")
include(":presentation-debug")
include(":presentation-settings")
include(":synchronizer")
include(":core:test")
include(":presentation-settings")
