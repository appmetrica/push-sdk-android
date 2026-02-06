pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://developer.huawei.com/repo")
        maven("https://artifactory-external.vkpartner.ru/artifactory/maven")
    }
    versionCatalogs {
        create("pushLibs") {
            from(files("libs.versions.toml"))
        }
    }
}
