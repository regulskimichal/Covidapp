plugins {
    id("com.android.application") apply false
    kotlin("android") apply false
    kotlin("plugin.parcelize") apply false
    kotlin("kapt") apply false
    id("dagger.hilt.android.plugin") apply false
    id("androidx.navigation.safeargs.kotlin") apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
