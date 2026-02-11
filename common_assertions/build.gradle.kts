@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369 fixed in gradle 8.1
plugins {
    alias(pushLibs.plugins.appMetricaKotlinLibrary)
}

group = "io.appmetrica.analytics"

dependencies {
    implementation(pushLibs.assertj)
    implementation(pushLibs.kotlinReflect)
}
