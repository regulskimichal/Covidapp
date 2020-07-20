plugins {
    id("com.android.application") apply false
    kotlin("android") apply false
    kotlin("android.extensions") apply false
    kotlin("kapt") apply false
    id("dagger.hilt.android.plugin") apply false
    id("androidx.navigation.safeargs.kotlin") apply false
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
