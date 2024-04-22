# AppMetrica Push Adapter for plugins

**Optional module**.
Maven: `io.appmetrica.analytics:push-plugin-adapter:${VERSION}`.

This module is specifically designed for developers creating plugins that utilize the Push SDK.
It addresses the unique requirements and functionalities needed when integrating the Push SDK within plugins.

## Builds

### Assemble

`./gradlew :plugin-adapter:assembleRelease`

### Publish to MavenLocal

`./gradlew :plugin-adapter:publishReleasePublicationToMavenLocal`

### Tests

`./gradlew :plugin-adapter:testReleaseUnitTest :plugin-adapter:generateReleaseJacocoReport`

### Code style

`./gradlew :plugin-adapter:codequality`

### Check AAR API

`./gradlew :plugin-adapter:aarCheck`

### Regenerate AAR API dump

`./gradlew :plugin-adapter:aarDump`
