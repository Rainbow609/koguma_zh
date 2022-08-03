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
            androidx()
            compose()
            hilt()
            kotlinx()
        }
    }
}

rootProject.name = "Koguma"
include("", "app")

fun VersionCatalogBuilder.androidx() {
    library("androidx-viewmodel-compose","androidx.lifecycle", "lifecycle-viewmodel-compose").version("2.5.1")
    library("androidx-corektx","androidx.core", "core-ktx").version("1.8.0")
    library("androidx-lifecycle-runtimektx","androidx.lifecycle", "lifecycle-runtime-ktx").version("2.5.1")
    library("androidx-activity-compose","androidx.activity", "activity-compose").version("1.5.1")
    library("androidx-junit","androidx.test.ext", "junit").version("1.1.3")
    library("androidx-espresso","androidx.test.espresso", "espresso-core").version("3.4.0")
}

fun VersionCatalogBuilder.compose() {
    val compose = version("compose", "1.2.0-rc02")

    library("compose-icons", "androidx.compose.material", "material-icons-extended").versionRef(compose)
    library("compose-ui-core", "androidx.compose.ui", "ui").versionRef(compose)
    library("compose-ui-toolingpreview", "androidx.compose.ui", "ui-tooling-preview").versionRef(compose)
    library("compose-material3", "androidx.compose.material3", "material3").version("1.0.0-alpha15")
    library("compose-junit", "androidx.compose.ui", "ui-test-junit4").versionRef(compose)
    library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").versionRef(compose)
    library("compose-ui-testmanifest", "androidx.compose.ui", "ui-test-manifest").versionRef(compose)
}

fun VersionCatalogBuilder.hilt() {
    val hilt = version("hilt", "2.38.1")

    library("hilt-android","com.google.dagger","hilt-android").versionRef(hilt)
    library("hilt-compiler","com.google.dagger","hilt-compiler").versionRef(hilt)
}

fun VersionCatalogBuilder.kotlinx() {
    library("kotlinx-serialization","org.jetbrains.kotlinx","kotlinx-serialization-json").version("1.4.0-RC")
}