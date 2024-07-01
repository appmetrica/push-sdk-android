import io.appmetrica.analytics.gradle.PushConstants
import io.appmetrica.analytics.gradle.PushDeps

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

        buildConfigField("boolean", "PUSH_DEBUG", "false")
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "PUSH_DEBUG", "true")
        }
    }
}

dependencies {
    compileOnly("io.appmetrica.analytics:analytics:${PushDeps.analytics}")
    testImplementation("io.appmetrica.analytics:analytics:${PushDeps.analytics}")

    implementation("com.squareup.okhttp3:okhttp:${PushDeps.okHttp}")

    api(project(":core-utils"))
    api(project(":provider-api"))
    implementation(project(":provider-firebase"))
    implementation(project(":logger"))
}
