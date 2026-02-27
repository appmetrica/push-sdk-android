@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369 fixed in gradle 8.1
plugins {
    alias(pushLibs.plugins.appMetricaGradlePlugin)
}

group = "io.appmetrica.analytics.gradle"

fun GradlePluginDevelopmentExtension.plugin(name: String, impl: String) {
    plugins.create(name.split('.', '-').joinToString("") { it.replaceFirstChar { it.uppercase() } }) {
        id = name
        implementationClass = impl
    }
}

gradlePlugin {
    plugin("appmetrica-jacoco", "io.appmetrica.analytics.gradle.jacoco.JacocoPlugin")
    plugin("appmetrica-proto", "io.appmetrica.analytics.gradle.protobuf.ProtobufPlugin")
    plugin("appmetrica-public-publish", "io.appmetrica.analytics.gradle.publishing.PublicPublishPlugin")
    plugin("appmetrica-publish", "io.appmetrica.analytics.gradle.publishing.PublishingPlugin")
    plugin("appmetrica-teamcity", "io.appmetrica.analytics.gradle.teamcity.TeamCityPlugin")
    plugin("appmetrica-update-push-version", "io.appmetrica.analytics.gradle.UpdatePushVersionPlugin")
    plugin("push-module", "io.appmetrica.analytics.gradle.AppMetricaPushModulePlugin")
}

dependencies {
    implementation(pushLibs.appMetricaAarCheck)
    implementation(pushLibs.appMetricaAndroidLibrary)
    implementation(pushLibs.appMetricaJacoco)
    implementation(pushLibs.appMetricaMavenCentralPublish)
    implementation(pushLibs.appMetricaNoLogs)
}
