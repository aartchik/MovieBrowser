plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.moviebrowser"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.moviebrowser"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {


    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    // Kotlin stdlib
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")

    // Core KTX
    implementation("androidx.core:core-ktx:1.10.1")

    // Activity + Compose integration — дает setContent { … }
    implementation("androidx.activity:activity-compose:1.9.0")

    // Lifecycle (ViewModel + runtime)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Compose BOM + основные библиотеки Compose
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Coil
    implementation("io.coil-kt:coil-compose:2.3.0")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.0")


    // Material Icons (для звёздочек)
    implementation("androidx.compose.material:material-icons-core:1.6.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
}
