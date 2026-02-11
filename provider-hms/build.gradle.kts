import io.appmetrica.analytics.gradle.PushConstants

plugins {
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-provider-hms")
    name.set("AppMetrica Push HMS provider")
    withJavadoc.set(true)
}

android {
    namespace = "io.appmetrica.analytics.push.provider.hms"
    defaultConfig {
        minSdk = PushConstants.Android.minSdkVersionHms
    }
}

dependencies {
    implementation(project(":core-utils"))
    implementation(project(":provider-api"))
    implementation(project(":logger"))

    compileOnly(pushLibs.hmsPush)
    testImplementation(pushLibs.hmsPush)
}
