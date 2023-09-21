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
