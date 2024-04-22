import io.appmetrica.analytics.gradle.PushDeps

plugins {
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-plugin-adapter")
    name.set("AppMetrica Push Adapter for plugins")
    withJavadoc.set(false)
}

android {
    namespace = "io.appmetrica.analytics.push.plugin.adapter"
}

dependencies {
    compileOnly(project(":push"))
    testImplementation(project(":push"))

    compileOnly("com.google.firebase:firebase-messaging:${PushDeps.fcm}")
    testImplementation("com.google.firebase:firebase-messaging:${PushDeps.fcm}")

    compileOnly(project(":provider-firebase"))
    testImplementation(project(":provider-firebase"))

    compileOnly("io.appmetrica.analytics:analytics:${PushDeps.analytics}")
    testImplementation("io.appmetrica.analytics:analytics:${PushDeps.analytics}")
}
