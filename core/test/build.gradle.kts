plugins {
    alias(libs.plugins.notifgram.android.library)
//    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.notifgram.core.test"
}

dependencies {
    api(projects.core.common)
    implementation(projects.core.dataRepository)
    implementation(projects.core.domain)

//    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.core)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.junit4)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)

    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.testManifest)
//    debugApi(libs.androidx.compose.ui.testManifest)
//    implementation(libs.core.ktx)
//    implementation(libs.appcompat)
////    testImplementation(libs.junit4)
//    androidTestImplementation(libs.androidx.test.ext.junit)
//    androidTestImplementation(libs.androidx.test.espresso.core)
}