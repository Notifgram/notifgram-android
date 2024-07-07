import com.android.ide.common.util.toPathString

plugins {
    alias(libs.plugins.com.android.application)
    id("notifgram.android.application.jacoco")
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.devtools.ksp)
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
//    id("androidx.benchmark")
    id("org.sonarqube")
    //id("jacoco")
    id("org.jetbrains.kotlinx.kover")
    id("com.google.gms.google-services")// Must be at the end
}

android {
    namespace = "com.notifgram"
    compileSdk = libs.versions.defaultCompileSdkVersion.get().toInt()

    defaultConfig {
        applicationId = "com.notifgram"
        minSdk = libs.versions.defaultMinSdkVersion.get().toInt()

        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.notifgram.core.common.AppTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = libs.versions.isMinifyEnabled.get().toBoolean()

            isShrinkResources = libs.versions.isMinifyEnabled.get().toBoolean()
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            enableAndroidTestCoverage = true
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    kover(project(":core:common"))
    kover(project(":core:data-file"))
    kover(project(":core:data-datastore"))
    kover(project(":core:data-local"))
    kover(project(":core:data-remote"))
    kover(project(":core:data-repository"))
    kover(project(":core:domain"))
    kover(project(":core:presentation-core"))
    kover(project(":presentation-channel"))
    kover(project(":presentation-home"))
    kover(project(":presentation-post"))
//    kover(project(":presentation-debug"))
    kover(project(":presentation-settings"))
    kover(project(":synchronizer"))


    implementation(project(":core:common"))
    implementation(project(":core:data-file"))
    implementation(project(":core:data-datastore"))
    implementation(project(":core:data-local"))
    implementation(project(":core:data-remote"))
    implementation(project(":core:data-repository"))
    implementation(project(":core:domain"))
    implementation(project(":core:presentation-core"))
    implementation(project(":presentation-channel"))
    implementation(project(":presentation-home"))
    implementation(project(":presentation-post"))
    implementation(project(":presentation-debug"))
    implementation(project(":presentation-settings"))
    implementation(project(":synchronizer"))

    implementation(libs.landscapist.coil) // FOR fcm screen
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.accompanist.permissions)

    implementation(libs.kotlinx.serialization.json)

    
    implementation(libs.security.crypto.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.inappmessaging.display)

    implementation(libs.compose.material3)
    implementation(libs.ui.tooling.preview)

    // Dagger-Hilt
    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.androidx.junit.ktx)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.test.mockk)
    testImplementation(libs.androidx.test.runner)

    androidTestImplementation(libs.test.mockk)
    androidTestImplementation(libs.androidx.test.runner)
}

sonarqube {
    properties {
        val libraries =
            "${project.android.sdkDirectory.toPathString()}/platforms/android-34/android.jar"
        /* + ", build/intermediates/exploded-aar*//**//*classes.jar"*/
        property("sonar.projectName", "${project.parent}-${project.name}")
        property("sonar.sources", "src/main/kotlin")
        property("sonar.binaries", "build/intermediates/app_classes/debug")
        property("sonar.libraries", libraries)
        property("sonar.tests", "src/test/kotlin, src/androidTest/kotlin")
        property("sonar.java.test.binaries", "build/intermediates/app_classes/debug")
        property("sonar.java.test.libraries", libraries)
        property("sonar.jacoco.reportPath", "build/jacoco/testDebugUnitTest.exec")
        property("sonar.java.coveragePlugin", "jacoco")

        //        property ("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/kover/reportDebug.xml")

        property("sonar.junit.reportsPath", "build/test-results/debug")
        property("sonar.android.lint.report", "build/outputs/lint-results-debug.xml")
//        property("sonar.kotlin.detekt.reportPaths", "build/reports/detekt.xml")
    }
}