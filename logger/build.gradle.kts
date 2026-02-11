plugins {
    id("push-module")
}

publishingInfo {
    baseArtifactId.set("push-logger")
    name.set("AppMetrica Push Logger")
}

android {
    namespace = "io.appmetrica.analytics.push.logger"
}

dependencies {
    api(pushLibs.analyticsCommonLogger)
}
