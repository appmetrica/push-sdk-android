# AppMetrica Push module

**Required module**.
Maven: `io.appmetrica.analytics:push:${VERSION}`.

## Builds

### Assemble

`./gradlew :push:assembleRelease`

### Publish to MavenLocal

`./gradlew :push:publishReleasePublicationToMavenLocal`

### Tests

`./gradlew :push:testReleaseUnitTest :push:generateReleaseJacocoReport`

### Code style

`./gradlew :push:codequality`

### Check AAR API

`./gradlew :push:aarCheck`

### Regenerate AAR API dump

`./gradlew :push:aarDump`
