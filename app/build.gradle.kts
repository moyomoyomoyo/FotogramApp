plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // ksp per Room
    id("com.google.devtools.ksp")
    // chiamate di rete
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.myfotogramapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myfotogramapp"
        minSdk = 36
        targetSdk = 36
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // CardView
    implementation("androidx.cardview:cardview:1.0.0")

    // implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.10.0") // Google Fonts
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0") // ViewModel
    implementation("androidx.datastore:datastore-preferences:1.2.0") // DataStore delle preferenze
    implementation("androidx.datastore:datastore:1.2.0") // DataStore

    // Room
    val room_version = "2.8.4"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // Chiamata di rete
    implementation(platform("io.ktor:ktor-bom:3.3.2"))
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-okhttp")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-client-logging:2.3.7")

    // Posizione
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")


    // Mapbox
    implementation("com.mapbox.maps:android-ndk27:11.17.1")
    implementation("com.mapbox.extension:maps-compose-ndk27:11.17.1")

    // Material3 e icone
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
}