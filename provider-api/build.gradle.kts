import io.appmetrica.analytics.gradle.PushDeps

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-provider-api")
    name.set("AppMetrica Push API for providers")
    withJavadoc.set(true)
}

android {
    namespace = "io.appmetrica.analytics.push.provider.api"
}

dependencies {
}
