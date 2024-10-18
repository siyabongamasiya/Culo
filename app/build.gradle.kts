plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id ("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "music.project.culo"
    compileSdk = 34

    defaultConfig {
        applicationId = "music.project.culo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "music.project.culo.HiltRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    flavorDimensions += listOf("advertised","pausable")
    productFlavors {
        create("with_ads"){
            dimension = "advertised"
            applicationIdSuffix = ".no_adv"
        }

        create("without_ads"){
            dimension = "advertised"
            applicationIdSuffix = ".adv"
        }

        create("pausable"){
            dimension = "pausable"
            applicationIdSuffix = ".pause"
        }

        create("not_pausable"){
            dimension = "pausable"
            applicationIdSuffix = ".not_pause"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

androidComponents{
    beforeVariants {builder ->
        builder.productFlavors.forEach {pair ->
            if (builder.buildType == "release"){
                if(pair.second == "without_ads" || pair.second == "not_pausable") {
                    builder.enable = false
                }
            }

            if (builder.buildType == "debug"){
                if(pair.second == "with_ads" || pair.second == "pausable") {
                    builder.enable = false
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.material.icons.extended.android)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.compose)
    implementation (libs.gson)

    implementation(libs.coil.compose)
    implementation(libs.coil.video)
    //admob
    implementation("com.google.android.gms:play-services-ads:23.3.0")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    //room & kapt
    val room_version = "2.6.1"
    implementation(libs.androidx.room.runtime.v261)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx.v261)
    annotationProcessor(libs.androidx.room.compiler.v261)
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")

    //FFMPEG
    //implementation("com.arthenica:ffmpeg-kit-full:6.0-2")
    implementation("com.arthenica:ffmpeg-kit-full-gpl:4.5.1-1")
    //implementation("com.arthenica:mobile-ffmpeg-full:4.4")

    //turbine for testing flows
    implementation("app.cash.turbine:turbine:1.1.0")

    implementation ("com.github.siyabongamasiya:Culo:1.0.0")

    testImplementation(libs.junit)
    testImplementation("com.google.truth:truth:1.0.1")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    androidTestImplementation("com.google.truth:truth:1.0.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation (libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}


task("hello world"){
    doFirst {
        println("my first gradle task in the beginning")
    }

    doLast {
        println("my first gradle task in the end")
    }
}

