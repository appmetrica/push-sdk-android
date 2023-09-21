# AppMetrica Push GCM provider module

**Optional module**.
Maven: `io.appmetrica.analytics:push-provider-gcm:${VERSION}`.

## Builds

### Assemble

`./gradlew :provider-gcm:assembleRelease`

### Publish to MavenLocal

`./gradlew :provider-gcm:publishReleasePublicationToMavenLocal`

### Tests

`./gradlew :provider-gcm:testReleaseUnitTest :provider-gcm:generateReleaseJacocoReport`

### Code style

`./gradlew :provider-gcm:codequality`

### Check AAR API

`./gradlew :provider-gcm:aarCheck`

### Regenerate AAR API dump

`./gradlew :provider-gcm:aarDump`
