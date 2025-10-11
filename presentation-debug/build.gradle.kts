plugins {
    alias(libs.plugins.notifgram.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.compose)

}

android {
    namespace = "com.notifgram.presentation_debug"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
}

dependencies {
    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.android.compiler)
    testImplementation(libs.junit4)
    implementation(libs.hilt.navigation.compose)

    implementation(project(":core:domain"))
    implementation(project(":core:presentation-core"))
    implementation(project(":core:common"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.ui)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.compose.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    debugImplementation(libs.ui.tooling)
    implementation(libs.lifecycle.runtime.ktx)


    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)

    androidTestImplementation(libs.junit4)
    implementation(libs.androidx.monitor)
    implementation(libs.androidx.junit.ktx)


}