plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.cc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cc"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures {
            viewBinding = true
        }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {
    // Add the CardView dependency
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation(platform("com.google.firebase:firebase-bom:33.4.0")) // Use BoM for version management
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.firebase:firebase-firestore:24.8.1")
    implementation ("com.google.firebase:firebase-storage:21.0.1")
    implementation ("com.google.android.gms:play-services-auth:20.6.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.preference)
    implementation(libs.lottie)
    implementation("com.hbb20:ccp:2.5.1")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.0")
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}


