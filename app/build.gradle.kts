plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.dinesh.library"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dinesh.library"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(JavaVersion.VERSION_17.toString().toInt())
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    implementation(project(":permission"))
    implementation(project(mapOf("path" to ":permission")))
//    implementation("com.github.Dinesh2811:EasyPermissionRequest:1.0")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.5.0")

    implementation("com.google.android.material:material:1.11.0-alpha02")
    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.material3:material3:1.2.0-alpha06")
}

