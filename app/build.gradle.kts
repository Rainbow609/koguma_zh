@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.google.services.get().pluginId)
    id(libs.plugins.firebase.crashlytics.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.aboutlibraries)
}

android {
    namespace = "me.ghostbear.koguma"
    compileSdk = 33

    defaultConfig {
        applicationId = "me.ghostbear.koguma"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            // useSupportLibrary true
        }
    }

    buildTypes {
        named("debug") {
            applicationIdSuffix = ".debug"
        }
        named("release") {
            isShrinkResources = true
            isMinifyEnabled = true
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs += arrayOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0-rc02"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.kotlinx.serialization)

    implementation(libs.bundles.ktor)

    implementation(libs.androidx.viewmodel.compose)

    implementation(libs.compose.icons)

    implementation(libs.compose.navigation)
    implementation(libs.hilt.navigation)

    implementation(libs.bundles.aboutlibraries)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.androidx.corektx)
    implementation(libs.androidx.lifecycle.runtimektx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui.core)
    implementation(libs.compose.ui.toolingpreview)
    implementation(libs.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.testmanifest)
}

tasks {
    preBuild {
        dependsOn(ktlintFormat)
    }
}
