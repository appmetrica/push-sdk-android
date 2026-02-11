import io.appmetrica.analytics.gradle.PushConstants

plugins {
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push")
    name.set("AppMetrica Push SDK")
    withJavadoc.set(true)
}

android {
    namespace = "io.appmetrica.analytics.push"
    defaultConfig {
        buildConfigField("int", "VERSION_CODE", "${PushConstants.Library.versionCode}")
        buildConfigField("String", "VERSION_NAME", "\"${PushConstants.Library.versionName}\"")
    }
}

dependencies {
    compileOnly(pushLibs.analytics)
    testImplementation(pushLibs.analytics)

    implementation(pushLibs.okhttp)

    api(project(":core-utils"))
    api(project(":provider-api"))
    implementation(project(":provider-firebase"))
    implementation(project(":logger"))
}
