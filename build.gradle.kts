// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath(libs.google.services)
    }

}


plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    id("androidx.benchmark") version "1.1.1" apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.jlleitschuh.ktlint) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.sonarqube) apply false
    alias(libs.plugins.hilt) apply false

}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}