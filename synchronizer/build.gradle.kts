import com.android.ide.common.util.toPathString

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("notifgram.android.library.jacoco")
    alias(libs.plugins.com.google.devtools.ksp)
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
//    id("androidx.benchmark")
    id("org.jetbrains.kotlinx.kover")
    id("org.sonarqube")
//    id("jacoco")
}

android {
    namespace = "com.notifgram.synchronizer"
    compileSdk = libs.versions.defaultCompileSdkVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.defaultMinSdkVersion.get().toInt()
        testInstrumentationRunner = "com.notifgram.core.common.AppTestRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = libs.versions.isMinifyEnabled.get().toBoolean()
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
}

dependencies {
    implementation(project(":core:data-file"))      // For injecting FileDir into tests
    implementation(project(":core:data-datastore"))
    implementation(project(":core:data-repository"))
    implementation(project(":core:data-local"))
    implementation(project(":core:data-remote"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    implementation(platform(libs.firebase.bom))

    implementation(libs.hilt.android)
    ksp(libs.androidx.hilt.compiler)   //Used for adding parameter to worker
    ksp(libs.hilt.compiler)
//    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.tracing.ktx)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.kotlin.test.junit)
    implementation(libs.core.ktx)
//    implementation(libs.appcompat)
    implementation(libs.android.material)
    androidTestImplementation(libs.androidx.test.ext.junit)
//    androidTestImplementation(libs.androidx.test.espresso.core)
}

//sonarqube {
//    properties {
//        val libraries =
//            "${project.android.sdkDirectory.toPathString()}/platforms/android-34/android.jar"
//        /* + ", build/intermediates/exploded-aar*//**//*classes.jar"*/
//        property("sonar.projectName", "${project.parent}-${project.name}")
//        property("sonar.sources", "src/main/kotlin")
//        property("sonar.binaries", "build/intermediates/app_classes/debug")
//        property("sonar.libraries", libraries)
////        property("sonar.tests", "src/test/kotlin, src/androidTest/kotlin")
//        property("sonar.java.test.binaries", "build/intermediates/app_classes/debug")
//        property("sonar.java.test.libraries", libraries)
//        property("sonar.jacoco.reportPath", "build/jacoco/testDebugUnitTest.exec")
//        property("sonar.java.coveragePlugin", "jacoco")
//
//        //        property ("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml")
//        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/kover/reportDebug.xml")
//
//        property("sonar.junit.reportsPath", "build/test-results/debug")
//        property("sonar.android.lint.report", "build/outputs/lint-results-debug.xml")
////        property("sonar.kotlin.detekt.reportPaths", "build/reports/detekt.xml")
//    }
//}