plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":sharedBUBOC"))
                implementation("androidx.appcompat:appcompat:1.6.0")
                implementation("androidx.activity:activity-compose:1.6.1")
            }
        }
    }
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "we.rashchenko.buboc"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}