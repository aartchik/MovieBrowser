// Файл: app/build.gradle.kts

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

        // Если будете использовать в интернете какие-то API-ключи, их можно прописать здесь:
        // buildConfigField("String", "OMDB_API_KEY", "\"ваш_ключ_здесь\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            // proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            // Для отладки
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // Включаем Compose
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0" // или более новая версия, совместимая с вашей версией Compose
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        }
    }
}



dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Compose
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Корутинсы
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Загрузка картинок (постеры фильмов)
    implementation("io.coil-kt:coil-compose:2.3.0")

    // DataStore Preferences (для хранения списка избранных фильмов)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Material Icons (если используете иконки в Compose)
    implementation("androidx.compose.material:material-icons-core:1.6.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    // (Опционально) для отладочной визуализации
    implementation("androidx.compose.material:material:1.5.0")
}
