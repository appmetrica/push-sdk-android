package io.appmetrica.analytics.push.impl.utils

internal object ReflectionUtils {

    @JvmStatic
    fun findClass(className: String): Class<*>? = try {
        Class.forName(className, false, ReflectionUtils::class.java.classLoader)
    } catch (ignored: Throwable) {
        null
    }

    @JvmStatic
    fun detectClassExists(className: String): Boolean = findClass(className) != null
}
