rootProject.name = "Covidapp"
include(":app")

pluginManagement {
    val gradleAndroidPluginVersion: String by settings
    val kotlinVersion: String by settings
    val navVersion: String by settings
    val hiltVersion: String by settings

    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application" -> useModule("com.android.tools.build:gradle:$gradleAndroidPluginVersion")
                "org.jetbrains.kotlin.android" -> useModule("org.jetbrains.kotlin.android:org.jetbrains.kotlin.android.gradle.plugin:$kotlinVersion")
                "org.jetbrains.kotlin.android.extensions" -> useModule("org.jetbrains.kotlin.android.extensions:org.jetbrains.kotlin.android.extensions.gradle.plugin:$kotlinVersion")
                "org.jetbrains.kotlin.kapt" -> useModule("org.jetbrains.kotlin.kapt:org.jetbrains.kotlin.kapt.gradle.plugin:$kotlinVersion")
                "androidx.navigation.safeargs.kotlin" -> useModule("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
                "dagger.hilt.android.plugin" -> useModule("com.google.dagger:hilt-android-gradle-plugin:2.28.3-alpha")
            }
        }
    }
}
