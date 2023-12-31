# AppMetrica Push Core Utils

**Required module**.
Maven: `io.appmetrica.analytics:push-core-utils:${VERSION}`.

## Builds

### Assemble

`./gradlew :core-utils:assembleRelease`

### Publish to MavenLocal

`./gradlew :core-utils:publishReleasePublicationToMavenLocal`

### Tests

`./gradlew :core-utils:testReleaseUnitTest :core-utils:generateReleaseJacocoReport`

### Code style

`./gradlew :core-utils:codequality`

### Check AAR API

`./gradlew :core-utils:aarCheck`

### Regenerate AAR API dump

`./gradlew :core-utils:aarDump`
