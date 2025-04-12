plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.travelexpertmobileapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.travelexpertmobileapplication"
        minSdk = 24
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
}

dependencies {
    implementation(libs.logging.interceptor)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.0")
    implementation ("com.jakewharton.timber:timber:4.7.1")
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")
    // Google Maps
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.libraries.places:places:3.3.0")
    // For permission handling
    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation ("com.google.android.material:material:1.11.0")
    // Dependencies for RecyclerView and CardView
    implementation ("androidx.recyclerview:recyclerview:1.3.0")
    implementation ("androidx.cardview:cardview:1.0.0")
}