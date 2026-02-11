plugins {
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

    compileOnly(pushLibs.playServicesBase)
    testImplementation(pushLibs.playServicesBase)

    compileOnly(pushLibs.firebaseMessaging)
    testImplementation(pushLibs.firebaseMessaging)
}
