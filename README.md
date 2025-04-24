# [AppMetrica Push SDK](https://appmetrica.io)

The AppMetrica Push SDK is a set of libraries for working with push notifications. After enabling the AppMetrica Push SDK, you can create and configure push notification campaigns, then monitor statistics in the AppMetrica web interface.
Detailed information and instructions for integration are available in the [documentation](https://appmetrica.io/docs/).

## Builds

### Assemble

`./gradlew :assembleRelease`

### Publish to MavenLocal

`./gradlew :publishReleasePublicationToMavenLocal`

### Tests

`./gradlew :testReleaseUnitTest :generateReleaseJacocoReport`

### Code style

`./gradlew :codequality`

### Check AAR API

`./gradlew :aarCheck`

### Regenerate AAR API dump

`./gradlew :aarDump`

## Modules

### Optional modules

The modules described below are optional and can be forcibly disabled from the AppMetrica SDK if necessary.
To do this, add the following code to the `app/build.gradle.kts` file:
```kotlin
configurations.configureEach {
    exclude(group = "io.appmetrica.analytics", module = "push-{module_name}")
}
```

#### Included by default

- **provider-firebase** - allows AppMetrica Push SDK to work with `com.google.firebase:firebase-messaging`.

#### Not included by default

- **plugin-adapter** - module for plugin developers.
- **provider-gcm** - allows AppMetrica Push SDK to work with `com.google.android.gms:play-services-gcm`.
- **provider-hms** - allows AppMetrica Push SDK to work with `com.huawei.hms:push`.
- **provider-rustore** - allows AppMetrica Push SDK to work with `ru.rustore.sdk:pushclient`.

### Module dependencies

You can find list of module dependencies with supported versions in the [dependencies_versions.yaml](dependencies_versions.yaml) file.
