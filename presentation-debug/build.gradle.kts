plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "com.notifgram.presentation_debug"
    compileSdk = libs.versions.defaultCompileSdkVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.defaultMinSdkVersion.get().toInt()

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
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
    implementation(libs.compose.material)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.compose.material)
    implementation(libs.android.material)
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