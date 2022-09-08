pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            general()
            aboutLibraries()
            androidx()
            compose()
            firebase()
            hilt()
            kotlinx()
            ktor()
        }
    }
}

rootProject.name = "Koguma"
include("", "app")
include(":core")
include(":data")
include(":domain")
include(":presentation")

fun VersionCatalogBuilder.general() {
    library("junit", "junit", "junit").version("4.13.2")

    plugin("ktlint", "org.jlleitschuh.gradle.ktlint").version("10.3.0")
}

fun VersionCatalogBuilder.aboutLibraries() {
    val version = version("aboutlibraries", "10.4.0")

    library("aboutlibraries-core", "com.mikepenz", "aboutlibraries-core").versionRef(version)

    plugin("aboutlibraries", "com.mikepenz.aboutlibraries.plugin").versionRef(version)

    bundle("aboutlibraries", listOf("aboutlibraries-core"))
}

fun VersionCatalogBuilder.androidx() {
    val agp = version("agp", "7.3.0-rc01")

    library("androidx-viewmodel-compose", "androidx.lifecycle", "lifecycle-viewmodel-compose").version("2.5.1")
    library("androidx-corektx", "androidx.core", "core-ktx").version("1.8.0")
    library("androidx-lifecycle-runtimektx", "androidx.lifecycle", "lifecycle-runtime-ktx").version("2.5.1")
    library("androidx-activity-compose", "androidx.activity", "activity-compose").version("1.5.1")
    library("androidx-junit", "androidx.test.ext", "junit").version("1.1.3")
    library("androidx-espresso", "androidx.test.espresso", "espresso-core").version("3.4.0")

    plugin("android-application", "com.android.application").versionRef(agp)
    plugin("android-library", "com.android.library").versionRef(agp)
}

fun VersionCatalogBuilder.compose() {
    val compose = version("compose", "1.2.0")

    val navigation = version("navigation", "2.5.1")

    library("compose-navigation", "androidx.navigation", "navigation-compose").versionRef(navigation)

    library("compose-icons", "androidx.compose.material", "material-icons-extended").versionRef(compose)
    library("compose-ui-core", "androidx.compose.ui", "ui").versionRef(compose)
    library("compose-ui-toolingpreview", "androidx.compose.ui", "ui-tooling-preview").versionRef(compose)
    library("compose-material3", "androidx.compose.material3", "material3").version("1.0.0-beta01")
    library("compose-junit", "androidx.compose.ui", "ui-test-junit4").versionRef(compose)
    library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").versionRef(compose)
    library("compose-ui-testmanifest", "androidx.compose.ui", "ui-test-manifest").versionRef(compose)
}

fun VersionCatalogBuilder.firebase() {
    library("google-services", "com.google.gms", "google-services").version("4.3.13")

    library("firebase-bom", "com.google.firebase", "firebase-bom").version("30.3.2")
    library("firebase-analytics-ktx", "com.google.firebase", "firebase-analytics-ktx").version("")
    library("firebase-crashlytics-ktx", "com.google.firebase", "firebase-crashlytics-ktx").version("")
    library("firebase-crashlytics-gradle", "com.google.firebase", "firebase-crashlytics-gradle").version("2.9.1")

    plugin("google-services", "com.google.gms.google-services").version("")
    plugin("firebase-crashlytics", "com.google.firebase.crashlytics").version("")

    bundle("firebase", listOf("firebase-analytics-ktx", "firebase-crashlytics-ktx"))
}

fun VersionCatalogBuilder.hilt() {
    val hilt = version("hilt", "2.43.2")

    library("hilt-android", "com.google.dagger", "hilt-android").versionRef(hilt)
    library("hilt-compiler", "com.google.dagger", "hilt-compiler").versionRef(hilt)
    library("hilt-navigation", "androidx.hilt", "hilt-navigation-compose").version("1.0.0")

    library("javax-inject", "javax.inject:javax.inject:1")

    plugin("hilt", "com.google.dagger.hilt.android").versionRef(hilt)
}

fun VersionCatalogBuilder.kotlinx() {
    val kotlin = version("kotlin", "1.7.10")

    library("kotlinx-serialization", "org.jetbrains.kotlinx", "kotlinx-serialization-json").version("1.4.0-RC")

    plugin("kotlinx-serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef(kotlin)
    plugin("kotlin-android", "org.jetbrains.kotlin.android").versionRef(kotlin)
    plugin("kotlin-kapt", "org.jetbrains.kotlin.kapt").version("")
}

fun VersionCatalogBuilder.ktor() {
    val ktor = version("ktor", "2.0.3")

    library("ktor-core", "io.ktor", "ktor-client-core").versionRef(ktor)
    library("ktor-cio", "io.ktor", "ktor-client-cio").versionRef(ktor)

    library("ktor-content-negotiation", "io.ktor", "ktor-client-content-negotiation").versionRef(ktor)
    library("ktor-serialization", "io.ktor", "ktor-serialization-kotlinx-json").versionRef(ktor)

    library("ktor-logging", "io.ktor", "ktor-client-logging").versionRef(ktor)

    bundle("ktor", listOf("ktor-core", "ktor-cio", "ktor-content-negotiation", "ktor-serialization", "ktor-logging"))
}
