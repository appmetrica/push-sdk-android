plugins {
    id("com.android.library")
    id("kotlin-android")
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-core-utils")
    name.set("AppMetrica Push Core Utils")
    withJavadoc.set(true)
}

android {
    namespace = "io.appmetrica.analytics.push.coreutils"
}

dependencies {
    implementation(project(":logger"))
}
