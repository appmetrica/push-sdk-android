package io.appmetrica.analytics.gradle

import org.gradle.api.Project

private const val DEFAULT_BUILD_NUMBER = 65535
private val BUILD_NUMBER = System.getenv("BUILD_NUMBER") ?: DEFAULT_BUILD_NUMBER.toString()

object PushConstants {
    const val robolectricSdk = 34 // after change run task `updateRobolectricSdk`

    object Android {
        const val buildToolsVersion = "35.0.0"
        const val sdkVersion = 35
        const val minSdkVersion = 21
        const val minSdkVersionHms = 21
        const val minSdkVersionRuStore = 23
    }

    object Library {
        const val versionName = "4.2.0"
        val versionCode = BUILD_NUMBER.toInt()
        val buildNumber = BUILD_NUMBER
        const val group = "io.appmetrica.analytics"
    }
}

object PushDeps {
    const val analytics = "7.0.0"

    const val fcm = "22.0.0"
    const val gcm = "17.0.0"
    const val playServices = "17.6.0"
    const val hms = "6.5.0.300"
    const val okHttp = "3.12.1" // check minSDK and proguard before update: https://github.com/square/okhttp/tree/master
}

val Project.isCIBuild: Boolean
    get() = project.hasProperty("sandbox")
