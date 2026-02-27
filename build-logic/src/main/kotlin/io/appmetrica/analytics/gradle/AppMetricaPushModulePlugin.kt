package io.appmetrica.analytics.gradle

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BuildType
import com.android.build.gradle.internal.dsl.DefaultConfig
import io.appmetrica.analytics.gradle.publishing.PublishingInfoBuildTypeExtension
import io.appmetrica.analytics.gradle.publishing.PublishingPlugin
import io.appmetrica.gradle.aarcheck.AarCheckExtension
import io.appmetrica.gradle.aarcheck.AarCheckPlugin
import io.appmetrica.gradle.aarcheck.agp.aarCheck
import io.appmetrica.gradle.android.plugins.AndroidLibraryPlugin
import io.appmetrica.gradle.jacoco.JacocoPlugin
import io.appmetrica.gradle.nologs.NoLogsExtension
import io.appmetrica.gradle.nologs.NoLogsPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.jvm.optionals.getOrNull

class AppMetricaPushModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply<AndroidLibraryPlugin>() // id("io.appmetrica.gradle.android-library")
        project.apply<JacocoPlugin>() // id("io.appmetrica.gradle.jacoco")
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
            compileSdk = PushConstants.Android.sdkVersion
            buildToolsVersion = PushConstants.Android.buildToolsVersion

            defaultConfig {
                minSdk = PushConstants.Android.minSdkVersion
                targetSdk = PushConstants.Android.sdkVersion

                this as DefaultConfig
                versionName = PushConstants.Library.versionName
                versionCode = PushConstants.Library.versionCode
            }
        }

        project.dependencies {
            val implementation by project.configurations.getting
            val testImplementation by project.configurations.getting
            val compileOnly by project.configurations.getting

            val pushLibs = project.versionCatalog("pushLibs")

            implementation(pushLibs["kotlinStdlib"]) // version is equal to plugin version
            compileOnly(pushLibs["androidxAnnotation"])
            compileOnly(pushLibs["androidxCore"])
            testImplementation(pushLibs["androidxCore"])
            testImplementation(pushLibs["androidxTestCore"])
        }
    }

    private fun Project.configureAndroid() {
        configure<LibraryExtension> {
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

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
            kotlinOptions {
                jvmTarget = "1.8"
            }
            if (name.lowercase().contains("releasekotlin")) {
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

            val pushLibs = project.versionCatalog("pushLibs")

            testImplementation(pushLibs["equalsverifier"])
            testImplementation(pushLibs["jsonassert"])
            testImplementation(pushLibs["commonAssertions"])
            testImplementation(findProject(":test-utils") ?: "io.appmetrica.analytics:test-utils")
        }
    }

    private fun Project.versionCatalog(name: String): VersionCatalog {
        return extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named(name)
    }

    private operator fun VersionCatalog.get(name: String): Any {
        return findLibrary(name).getOrNull()!!.get()
    }
}
