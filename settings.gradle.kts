pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // версия Android Gradle Plugin берётся из официа льных релизов — здесь советую 8.0.2
        id("com.android.application") version "8.0.2"
        id("org.jetbrains.kotlin.android") version "1.8.10"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieBrowser"
include(":app")
