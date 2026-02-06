import io.appmetrica.analytics.gradle.PushDeps

plugins {
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-provider-gcm")
    name.set("AppMetrica Push GCM provider")
    withJavadoc.set(true)
}

android {
    namespace = "io.appmetrica.analytics.push.provider.gcm"
}

dependencies {
    implementation(project(":core-utils"))
    implementation(project(":provider-api"))
    implementation(project(":logger"))

    compileOnly("com.google.android.gms:play-services-gcm:${PushDeps.gcm}")
    testImplementation("com.google.android.gms:play-services-gcm:${PushDeps.gcm}")
}
