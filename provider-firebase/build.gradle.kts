import io.appmetrica.analytics.gradle.PushDeps

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-provider-firebase")
    name.set("AppMetrica Push Firebase provider")
    withJavadoc.set(true)
}

android {
    namespace = "io.appmetrica.analytics.push.provider.firebase"
}

dependencies {
    implementation(project(":core-utils"))
    implementation(project(":provider-api"))
    implementation(project(":logger"))

    compileOnly("com.google.android.gms:play-services-base:${PushDeps.playServices}")
    testImplementation("com.google.android.gms:play-services-base:${PushDeps.playServices}")

    compileOnly("com.google.firebase:firebase-messaging:${PushDeps.fcm}")
    testImplementation("com.google.firebase:firebase-messaging:${PushDeps.fcm}")
}
