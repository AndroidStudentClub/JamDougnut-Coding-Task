plugins {
    id("com.google.devtools.ksp")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.0"
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.jamdoughnut.test"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jamdoughnut.test"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.kotlinx.serialization.json)

    // Compose

    implementation(libs.androidx.compose.bom)
    androidTestImplementation(libs.androidx.compose.bom)
    implementation(libs.androidx.ui.tooling.preview)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    implementation(libs.androidx.activity.compose)

    implementation (libs.androidx.navigation)

    implementation (libs.coil.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.appcompat)


    // Hilt for DI
    implementation (libs.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Test
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation (libs.mockito.android)

    testImplementation (libs.mockk)
    testImplementation (libs.mockitokotlin2.mockito.kotlin)
}