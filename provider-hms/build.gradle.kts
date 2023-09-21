import io.appmetrica.analytics.gradle.PushConstants
import io.appmetrica.analytics.gradle.PushDeps

plugins {
    id("com.android.library")
    id("kotlin-android")
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
        minSdkVersion(PushConstants.Android.minSdkVersionHms)
    }
}

dependencies {
    implementation(project(":core-utils"))
    implementation(project(":provider-api"))

    compileOnly("com.huawei.hms:push:${PushDeps.hms}")
    testImplementation("com.huawei.hms:push:${PushDeps.hms}")
}
