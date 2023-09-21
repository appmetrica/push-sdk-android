package io.appmetrica.analytics.push.impl.location.verifier

import android.location.Location
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier

class CompositeVerifier(
    private val verifiers: List<LocationVerifier>
) : LocationVerifier {

    constructor(vararg verifiers: LocationVerifier) : this(verifiers.toList())

    override fun verifyLocation(location: Location): LocationStatus {
        return verifiers
            .map { it.verifyLocation(location) }
            .firstOrNull { !it.isSuccess }
            ?: LocationStatus.Success()
    }
}
