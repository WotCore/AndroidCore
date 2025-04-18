pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidCore"

include(":_core")

// 自动引入 _lib 下的模块
val libDir = File(rootDir, "_lib")
if (libDir.exists() && libDir.isDirectory) {
    libDir.listFiles()?.forEach { file ->
        if (file.isDirectory && (File(file, "build.gradle.kts").exists() || File(file, "build.gradle.kts.kts").exists())) {
            val moduleName = file.name
            include(":_lib:$moduleName")
            project(":_lib:$moduleName").projectDir = file
        }
    }
}

include(":app")
