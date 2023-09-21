# AppMetrica Push HMS provider module

**Optional module**.
Maven: `io.appmetrica.analytics:push-provider-hms:${VERSION}`.

## Builds

### Assemble

`./gradlew :provider-hms:assembleRelease`

### Publish to MavenLocal

`./gradlew :provider-hms:publishReleasePublicationToMavenLocal`

### Tests

`./gradlew :provider-hms:testReleaseUnitTest :provider-hms:generateReleaseJacocoReport`

### Code style

`./gradlew :provider-hms:codequality`

### Check AAR API

`./gradlew :provider-hms:aarCheck`

### Regenerate AAR API dump

`./gradlew :provider-hms:aarDump`
