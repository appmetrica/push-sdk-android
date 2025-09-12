package io.appmetrica.analytics.gradle

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BuildType
import com.android.build.gradle.internal.dsl.DefaultConfig
import io.appmetrica.analytics.gradle.codequality.CodeQualityExtension
import io.appmetrica.analytics.gradle.codequality.CodeQualityPlugin
import io.appmetrica.analytics.gradle.jacoco.JacocoPlugin
import io.appmetrica.analytics.gradle.publishing.PublishingInfoBuildTypeExtension
import io.appmetrica.analytics.gradle.publishing.PublishingPlugin
import io.appmetrica.gradle.aarcheck.AarCheckExtension
import io.appmetrica.gradle.aarcheck.AarCheckPlugin
import io.appmetrica.gradle.aarcheck.agp.aarCheck
import io.appmetrica.gradle.android.plugins.AndroidLibrary
import io.appmetrica.gradle.nologs.NoLogsExtension
import io.appmetrica.gradle.nologs.NoLogsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Locale

class AppMetricaPushModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply<AndroidLibrary>() // id("io.appmetrica.gradle.android-library")
        project.apply<CodeQualityPlugin>() // id("appmetrica-codequality")
        project.apply<JacocoPlugin>() // id("appmetrica-jacoco")
        project.apply<PublishingPlugin>() // id("appmetrica-publish")
        project.apply<AarCheckPlugin>() // id("io.appmetrica.gradle.aar-check")
        project.apply<NoLogsPlugin>() // id("io.appmetrica.gradle.no-logs")

        project.group = PushConstants.Library.group

        project.configureAndroid()
        project.configureKotlin()
        project.configureAarCheck()
        project.configureNoLogs()
        project.configureTests()

        project.configure<LibraryExtension> {
            compileSdkVersion(PushConstants.Android.sdkVersion)
            buildToolsVersion(PushConstants.Android.buildToolsVersion)

            defaultConfig {
                minSdkVersion(PushConstants.Android.minSdkVersion)
                targetSdkVersion(PushConstants.Android.sdkVersion)

                this as DefaultConfig
                versionName = PushConstants.Library.versionName
                versionCode = PushConstants.Library.versionCode
            }
        }

        project.configure<CodeQualityExtension> {
            configDir.set(project.rootProject.layout.projectDirectory.dir("codequality"))
            exclude.addAll(
                "**/org/mockito/configuration/**",
            )
        }

        project.dependencies {
            val implementation by project.configurations.getting
            val testImplementation by project.configurations.getting
            val compileOnly by project.configurations.getting

            implementation("org.jetbrains.kotlin:kotlin-stdlib") // version is equal to plugin version
            compileOnly("androidx.annotation:annotation:1.5.0")
            compileOnly("androidx.core:core:1.9.0")
            testImplementation("androidx.core:core:1.9.0")
            testImplementation("androidx.test:core:1.5.0")
        }
    }

    private fun Project.configureAndroid() {
        configure<LibraryExtension> {
            defaultConfig {
                proguardFiles("proguard/proguard-rules.pro")
                consumerProguardFiles("proguard/consumer-rules.pro")
            }

            buildFeatures {
                buildConfig = true
            }

            buildTypes {
                debug {
                    isMinifyEnabled = false
                    configure<PublishingInfoBuildTypeExtension> {
                        artifactIdSuffix.set("-debug")
                    }
                }
                release {
                    isMinifyEnabled = true
                    aarCheck.enabled = true
                }
                create("snapshot") {
                    isMinifyEnabled = true
                    (this as BuildType).versionNameSuffix = "-SNAPSHOT"
                }
            }
        }
    }

    private fun Project.configureKotlin() {
        tasks.withType<KotlinCompile> {
            if (name.toLowerCase(Locale.ROOT).contains("releasekotlin")) {
                kotlinOptions {
                    freeCompilerArgs += listOf(
                        "-Xno-call-assertions",
                        "-Xno-receiver-assertions",
                        "-Xno-param-assertions"
                    )
                }
            }
        }
    }

    private fun Project.configureAarCheck() {
        configure<AarCheckExtension> {
            checkDependencies = true
            checkManifest = true
            checkModule = true
            checkPom = true
            checkProguard = true
            forbiddenImports = listOf(
                "io.appmetrica.analytics.push.coreutils.internal.utils.PLog",
            )
            forbiddenMethods = mapOf(
                "kotlin.jvm.internal.Intrinsics" to listOf(
                    "checkNotNullParameter",
                    "checkNotNullExpressionValue",
                    "checkReturnedValueIsNotNull",
                    "checkExpressionValueIsNotNull",
                    "checkFieldIsNotNull",
                    "checkParameterIsNotNull"
                )
            )
        }
    }

    private fun Project.configureNoLogs() {
        configure<NoLogsExtension> {
            loggerClasses = listOf(
                "DebugLogger",
                "DebugLogger.INSTANCE"
            )
            shouldRemoveLogs = { it.buildType.name == "release" }
        }
    }

    private fun Project.configureTests() {
        configure<LibraryExtension> {
            @Suppress("UnstableApiUsage")
            testOptions {
                unitTests {
                    isReturnDefaultValues = true
                    isIncludeAndroidResources = true
                    all {
                        // about arguments https://docs.oracle.com/javase/9/tools/java.htm
                        it.jvmArgs("-noverify", "-Xmx2g")
                        // https://nda.ya.ru/t/81uL3Dxs6Njj8v
                        it.jvmArgs("-Djdk.attach.allowAttachSelf=true")
                        // need for fix https://nda.ya.ru/t/PGGDmRNa6Njj8w
                        it.jvmArgs("-XX:CompileCommand=exclude,android/database/sqlite/SQLiteSession*.*")
                        it.systemProperty("robolectric.logging.enabled", "true")
                        it.maxParallelForks = 4
                        it.beforeTest(
                            closureOf<TestDescriptor> {
                                logger.lifecycle("< $this started.")
                            }
                        )
                        it.afterTest(
                            closureOf<TestDescriptor> {
                                logger.lifecycle("< $this finished.")
                            }
                        )
                    }
                }
            }

            @Suppress("UnstableApiUsage")
            useLibrary("org.apache.http.legacy")
        }

        dependencies {
            val testImplementation by configurations.getting

            testImplementation("nl.jqno.equalsverifier:equalsverifier:3.4.2")
            testImplementation("org.skyscreamer:jsonassert:1.5.0")
            testImplementation("io.appmetrica.analytics:common_assertions")
            testImplementation(findProject(":test-utils") ?: "io.appmetrica.analytics:test-utils")
        }
    }
}
