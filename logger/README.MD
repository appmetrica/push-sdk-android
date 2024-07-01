# AppMetrica Push Logger

**Required module**.
Maven: `io.appmetrica.analytics:push-logger:${VERSION}`.

## Builds

### Assemble

`./gradlew :logger:assembleRelease`

### Publish to MavenLocal

`./gradlew :logger:publishReleasePublicationToMavenLocal`

### Tests

`./gradlew :logger:testReleaseUnitTest :logger:generateReleaseJacocoReport`

### Code style

`./gradlew :logger:codequality`

### Check AAR API

`./gradlew :logger:aarCheck`

### Regenerate AAR API dump

`./gradlew :logger:aarDump`
