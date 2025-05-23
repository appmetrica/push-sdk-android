import io.appmetrica.gradle.extensions.detectAgpVersion

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369 fixed in gradle 8.1
plugins {
    alias(libs.plugins.appMetricaGradlePlugin)
}

group = "io.appmetrica.analytics.gradle"

val agpVersion: String = detectAgpVersion("8.2.0")

fun GradlePluginDevelopmentExtension.plugin(name: String, impl: String) {
    plugins.create(name.split('.', '-').joinToString("") { it.capitalize() }) {
        id = name
        implementationClass = impl
    }
}

gradlePlugin {
    plugin("appmetrica-codequality", "io.appmetrica.analytics.gradle.codequality.CodeQualityPlugin")
    plugin("appmetrica-jacoco", "io.appmetrica.analytics.gradle.jacoco.JacocoPlugin")
    plugin("appmetrica-proto", "io.appmetrica.analytics.gradle.protobuf.ProtobufPlugin")
    plugin("appmetrica-public-publish", "io.appmetrica.analytics.gradle.publishing.PublicPublishPlugin")
    plugin("appmetrica-publish", "io.appmetrica.analytics.gradle.publishing.PublishingPlugin")
    plugin("appmetrica-teamcity", "io.appmetrica.analytics.gradle.teamcity.TeamCityPlugin")
    plugin("appmetrica-update-push-version", "io.appmetrica.analytics.gradle.UpdatePushVersionPlugin")
    plugin("push-module", "io.appmetrica.analytics.gradle.AppMetricaPushModulePlugin")
}

dependencies {
    // https://developer.android.com/studio/releases/gradle-plugin
    implementation("com.android.tools.build:gradle:$agpVersion")
    // https://kotlinlang.org/docs/gradle.html
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    // https://detekt.dev/docs/gettingstarted/gradle/
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.3")
    // by source
    implementation(libs.appMetricaAarCheck)
    implementation(libs.appMetricaAndroid)
    implementation(libs.appMetricaNoLogs)
    implementation(libs.appMetricaMavenCentralPublish)
}
