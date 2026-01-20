import io.appmetrica.analytics.gradle.AppMetricaPushModulePlugin
import io.appmetrica.analytics.gradle.PushConstants

plugins {
    id("appmetrica-update-push-version")
    id("appmetrica-public-publish")
    alias(libs.plugins.appMetricaCheckNamespace)
}

group = PushConstants.Library.group
version = PushConstants.Library.versionName

val modules by lazy { subprojects.filter { it.plugins.hasPlugin(AppMetricaPushModulePlugin::class.java) } }
val buildTypes = listOf("release", "snapshot", "debug")

fun createTaskName(
    prefix: String,
    buildType: String = "",
    suffix: String = ""
): String {
    return "${prefix}${buildType.capitalize()}${suffix.capitalize()}"
}

buildTypes.forEach { buildType ->
    tasks.register("assemble${buildType.capitalize()}") {
        dependsOn(modules.map { it.tasks.named(createTaskName("assemble", buildType)) })
    }
    tasks.register("publish${buildType.capitalize()}PublicationToMavenLocal") {
        dependsOn(modules.map { it.tasks.named(createTaskName("publish", buildType, "publicationToMavenLocal")) })
    }
    tasks.register("test${buildType.capitalize()}UnitTest") {
        dependsOn(modules.map { it.tasks.named(createTaskName("test", buildType, "unitTest")) })
    }
    tasks.register("generate${buildType.capitalize()}JacocoReport") {
        dependsOn(modules.map { it.tasks.named(createTaskName("generate", buildType, "jacocoReport")) })
    }
}

tasks.register("codequality") {
    dependsOn(modules.map { it.tasks.named(createTaskName("codequality")) })
}
tasks.register("aarCheck") {
    dependsOn(modules.map { it.tasks.named(createTaskName("aarCheck")) })
}
tasks.register("aarDump") {
    dependsOn(modules.map { it.tasks.named(createTaskName("aarDump")) })
}
