# AppMetrica Push API for providers module

**Required module**.
Maven: `io.appmetrica.analytics:push-provider-api:${VERSION}`.

## Builds

### Assemble

`./gradlew :provider-api:assembleRelease`

### Publish to MavenLocal

`./gradlew :provider-api:publishReleasePublicationToMavenLocal`

### Tests

`./gradlew :provider-api:testReleaseUnitTest :provider-api:generateReleaseJacocoReport`

### Code style

`./gradlew :provider-api:codequality`

### Check AAR API

`./gradlew :provider-api:aarCheck`

### Regenerate AAR API dump

`./gradlew :provider-api:aarDump`
