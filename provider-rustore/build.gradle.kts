import io.appmetrica.analytics.gradle.PushConstants

plugins {
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-provider-rustore")
    name.set("AppMetrica Push RuStore provider")
    withJavadoc.set(true)
}

android {
    namespace = "io.appmetrica.analytics.push.provider.rustore"
    defaultConfig {
        minSdkVersion(PushConstants.Android.minSdkVersionRuStore)
    }
}

dependencies {
    implementation(project(":core-utils"))
    implementation(project(":provider-api"))
    implementation(project(":logger"))

    compileOnly("ru.rustore.sdk:pushclient:2.1.1")
    testImplementation("ru.rustore.sdk:pushclient:2.1.1")
}
