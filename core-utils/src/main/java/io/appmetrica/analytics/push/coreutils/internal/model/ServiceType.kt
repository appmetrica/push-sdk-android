package io.appmetrica.analytics.push.coreutils.internal.model

enum class ServiceType(
    val value: Int
) {
    PROVIDER_SERVICE(0),
    APPMETRICA_PUSH_SERVICE(1),
    UNKNOWN(-1);

    companion object {

        @JvmStatic
        fun fromValue(value: Int): ServiceType {
            for (type in values()) {
                if (type.value == value) {
                    return type
                }
            }
            return UNKNOWN
        }
    }
}
