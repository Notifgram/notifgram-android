import com.android.ide.common.util.toPathString

plugins {
    alias(libs.plugins.notifgram.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("notifgram.android.library.jacoco")
    alias(libs.plugins.com.google.devtools.ksp)
    id("kotlin-parcelize")
//    id("androidx.benchmark")
    id("org.jetbrains.kotlinx.kover")
    id("org.sonarqube")
    //id("jacoco")
}

android {
    namespace = "com.notifgram.core.common"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    implementation(libs.hilt.android.testing) // for AppTestRunner
    implementation(libs.androidx.test.runner) // for AppTestRunner
    testImplementation(libs.mockk)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
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