import io.appmetrica.analytics.gradle.PushDeps

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-logger")
    name.set("AppMetrica Push Logger")
}

android {
    namespace = "io.appmetrica.analytics.push.logger"
}

dependencies {
    api("io.appmetrica.analytics:analytics-common-logger:${PushDeps.analytics}")
}
