import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.example.benchmark"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixelCApi30") {
            device = "Pixel C"
            apiLevel = 31
            systemImageSource = "aosp"
        }
    }

    buildTypes {
            // This benchmark buildType is used for benchmarking, and should function like your
            // release build (for example, with minification on). It"s signed with a debug key
            // for easy local/CI testing.
            create("benchmark") {
                isDebuggable = true
                signingConfig = getByName("debug").signingConfig
                matchingFallbacks += listOf("release")
                proguardFiles("benchmark-rules.pro")
            }
        }

        targetProjectPath = ":app"
        experimentalProperties["android.experimental.self-instrumenting"] = true
    }

    dependencies {
        implementation(libs.androidx.junit)
        implementation(libs.androidx.espresso.core)
        implementation(libs.androidx.uiautomator)
        implementation(libs.androidx.benchmark.macro.junit4)
    }

    androidComponents {
        beforeVariants(selector().withBuildType("benchmark").all()) {
            it.enable = true
        }
    }
