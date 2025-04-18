plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "wot.core"
    compileSdkVersion(34)

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(34)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            // 调试版本的配置
        }
        getByName("release") {
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
}

dependencies {
    // 自动导入 _lib 下的模块
    val libModules = File(rootDir, "_lib")
        .listFiles()
        ?.filter { it.isDirectory && File(it, "build.gradle.kts").exists() }
        ?.map { ":_lib:${it.name}" }

    libModules?.forEach {
        api(project(it))
    }

    api("androidx.core:core-ktx:1.10.1")
    api("androidx.appcompat:appcompat:1.6.1")
}
