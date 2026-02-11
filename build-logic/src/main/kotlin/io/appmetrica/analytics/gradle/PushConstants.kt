package io.appmetrica.analytics.gradle

import org.gradle.api.Project

private const val DEFAULT_BUILD_NUMBER = 65535
private val BUILD_NUMBER = System.getenv("BUILD_NUMBER") ?: DEFAULT_BUILD_NUMBER.toString()

object PushConstants {
    const val robolectricSdk = 34 // after change run task `updateRobolectricSdk`

    object Android {
        const val buildToolsVersion = "36.0.0"
        const val sdkVersion = 36
        const val minSdkVersion = 21
        const val minSdkVersionHms = 21
        const val minSdkVersionRuStore = 23
    }

    object Library {
        const val versionName = "4.3.0"
        val versionCode = BUILD_NUMBER.toInt()
        val buildNumber = BUILD_NUMBER
        const val group = "io.appmetrica.analytics"
    }
}

val Project.isCIBuild: Boolean
    get() = project.hasProperty("sandbox")
