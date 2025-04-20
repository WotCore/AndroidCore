import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.api.artifacts.VersionCatalog

fun DependencyHandlerScope.implementation(dep: Any) {
    add("implementation", dep)
}

fun DependencyHandlerScope.kapt(dep: Any) {
    add("kapt", dep)
}

fun DependencyHandlerScope.addCoreDependencies(libs: VersionCatalog) {
    implementation(libs.findLibrary("core-ktx").get())
    implementation(libs.findLibrary("appcompat").get())
    implementation(libs.findLibrary("material").get())
    implementation(libs.findLibrary("constraintlayout").get())
}

fun DependencyHandlerScope.addLifecycleDependencies(libs: VersionCatalog) {
    implementation(libs.findLibrary("lifecycle-viewmodel").get())
    implementation(libs.findLibrary("lifecycle-livedata").get())
}

fun DependencyHandlerScope.addNavigationDependencies(libs: VersionCatalog) {
    implementation(libs.findLibrary("navigation-fragment").get())
    implementation(libs.findLibrary("navigation-ui").get())
}

fun DependencyHandlerScope.addCoroutinesDependencies(libs: VersionCatalog) {
    implementation(libs.findLibrary("coroutines-core").get())
    implementation(libs.findLibrary("coroutines-android").get())
}

fun DependencyHandlerScope.addRoomDependencies(libs: VersionCatalog) {
    implementation(libs.findLibrary("room-runtime").get())
    implementation(libs.findLibrary("room-ktx").get())
    kapt(libs.findLibrary("room-compiler").get())
}