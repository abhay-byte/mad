plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Read OpenWeather API key from project properties or local.properties (kept out of VCS)
val openWeatherKey: String? = (project.findProperty("OPENWEATHER_API_KEY") as String?)
    ?: run {
        // try root/local.properties
        val lp = rootProject.file("local.properties")
        if (lp.exists()) {
            val props = java.util.Properties()
            lp.reader().use { props.load(it) }
            props.getProperty("OPENWEATHER_API_KEY")
        } else null
    }

android {
    namespace = "com.example.experiment4"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.experiment4"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Expose the API key to the app at build time via BuildConfig (empty if not provided)
        buildConfigField("String", "OPENWEATHER_API_KEY", "\"${openWeatherKey ?: ""}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    val composeBomVersion = "2024.02.00"
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
