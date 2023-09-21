package io.appmetrica.analytics.push.impl.location

import io.appmetrica.analytics.push.location.LocationProvider

object LocationProviderHolder {

    @JvmStatic
    var provider: LocationProvider? = null
}
