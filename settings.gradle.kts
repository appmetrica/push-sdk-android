if (file("internal.settings.gradle.kts").exists()) {
    apply(from = "internal.settings.gradle.kts")
} else {
    apply(from = "public.settings.gradle.kts")
}

rootProject.name = "push-sdk"

// build scripts
includeBuild("build-logic") {
    name = "push-sdk-build-logic"
}

// modules
include("core-utils")
include("plugin-adapter")
include("provider-api")
include("provider-firebase")
include("provider-gcm")
include("provider-hms")
include("provider-rustore")
include("push")

// tests modules
includeBuild("common_assertions")
include("test-utils")
