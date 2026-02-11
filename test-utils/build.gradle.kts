plugins {
    alias(pushLibs.plugins.appMetricaKotlinLibrary)
}

dependencies {
    api(pushLibs.junit)
    api(pushLibs.assertj)
    api(pushLibs.mockitoInline)
    api(pushLibs.mockitoKotlin)
    // https://github.com/robolectric/robolectric
    api(pushLibs.robolectric)
}
