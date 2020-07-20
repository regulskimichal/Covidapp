import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature.PARCELIZE

val kotlinVersion: String by project
val jacksonVersion: String by project
val daggerVersion: String by project
val hiltVersion: String by project
val androidXHiltVersion: String by project
val gradleAndroidPluginVersion: String by project
val navVersion: String by project

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.1"

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        applicationId = "pl.gurbakregulski.covidapp"
        minSdkVersion(28)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mapOf()
                )
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    androidExtensions {
        features = setOf(PARCELIZE.featureName)
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.appcompat:appcompat:1.1.0")

    implementation("com.google.android.material:material:1.3.0-alpha01")

    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0-alpha04")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    kapt("androidx.databinding:databinding-compiler:$gradleAndroidPluginVersion")

    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$androidXHiltVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$androidXHiltVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest("com.google.dagger:hilt-android-compiler:$hiltVersion")

    implementation("com.google.android.gms:play-services-basement:17.3.0")
    implementation("com.google.android.gms:play-services-base:17.3.0")
    implementation("com.google.android.gms:play-services-gcm:17.0.0")
    implementation("com.google.android.gms:play-services-location:17.0.0")

    implementation("com.google.maps.android:maps-v3-ktx:2.0.0")
    implementation("com.google.maps.android:maps-utils-v3-ktx:2.0.0")
    implementation("com.google.android.libraries.maps:maps:3.1.0-beta")
    implementation("com.google.maps.android:android-maps-utils-v3:2.0.3")

    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("com.github.blongho:worldCountryData:1.5")

    testImplementation("junit:junit:4.13")

    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
}
