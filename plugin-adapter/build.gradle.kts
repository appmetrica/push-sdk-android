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

    compileOnly(pushLibs.firebaseMessaging)
    testImplementation(pushLibs.firebaseMessaging)

    compileOnly(project(":provider-firebase"))
    testImplementation(project(":provider-firebase"))

    compileOnly(pushLibs.analytics)
    testImplementation(pushLibs.analytics)
}
