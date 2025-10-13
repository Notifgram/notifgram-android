import com.android.ide.common.util.toPathString

plugins {
    alias(libs.plugins.notifgram.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.notifgram.android.library.jacoco)
    id("dagger.hilt.android.plugin")
    //id("jacoco")
    alias(libs.plugins.kover)
    alias(libs.plugins.com.google.devtools.ksp)
    id("org.sonarqube")
    alias(libs.plugins.notifgram.android.library.compose)

}

android {
    namespace = "com.notifgram.presentation_settings"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.hilt.android)
    implementation(projects.core.common)
    implementation(projects.core.dataRepository)
    implementation(projects.core.presentationCore)
    implementation(projects.core.domain)
    debugImplementation(libs.androidx.compose.ui.tooling)
    ksp(libs.hilt.android.compiler)

    implementation(libs.hilt.navigation.compose)


//    implementation(libs.core.ktx)
//    implementation(libs.appcompat)
    implementation(libs.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
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