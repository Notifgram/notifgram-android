import com.android.ide.common.util.toPathString


plugins {
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.android.library)
    id("notifgram.android.library.jacoco")
    id("org.jetbrains.kotlinx.kover")
    id("org.sonarqube")
//    id("jacoco")
    alias(libs.plugins.com.google.devtools.ksp)
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.notifgram.core.data_local"
    compileSdk = libs.versions.defaultCompileSdkVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.defaultMinSdkVersion.get().toInt()
        testInstrumentationRunner = "com.notifgram.core.common.AppTestRunner"

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:data-repository"))
    implementation(project(":core:data-datastore"))

    implementation(libs.sqlite.ktx)
    implementation(libs.android.database.sqlcipher)

    implementation(libs.security.crypto.ktx)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.junit.ktx)
    ksp(libs.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.test.mockk)

    androidTestImplementation(libs.kotlin.test.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)

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
//        property("sonar.tests", "src/test/kotlin, src/androidTest/kotlin")
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