import com.android.ide.common.util.toPathString

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("notifgram.android.library.jacoco")
    id("dagger.hilt.android.plugin")
    //id("jacoco")
    id("org.jetbrains.kotlinx.kover")
    alias(libs.plugins.com.google.devtools.ksp)
    id("org.sonarqube")
}

android {
    namespace = "com.notifgram.presentation_settings"
    compileSdk = libs.versions.defaultCompileSdkVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.defaultMinSdkVersion.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(libs.accompanist.permissions)
    implementation(libs.hilt.android)
    implementation(project(":core:common"))
    implementation(project(":core:data-repository"))
    implementation(project(":core:presentation-core"))
    implementation(project(":core:domain"))
    debugImplementation(libs.ui.tooling)
    ksp(libs.hilt.android.compiler)

    implementation(libs.hilt.navigation.compose)


//    implementation(libs.core.ktx)
//    implementation(libs.appcompat)
    implementation(libs.android.material)
    implementation(libs.compose.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.material.icons.extended)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
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