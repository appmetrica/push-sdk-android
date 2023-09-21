package io.appmetrica.analytics.push.impl.lazypush.rule

import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils
import io.appmetrica.analytics.push.impl.processing.transform.TransformFailureException
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule

class PushIdTransformRule(
    private val pushId: String?
) : LazyPushTransformRule {

    override fun getPatternList() = listOf(PATTERN)

    override fun getNewValue(pattern: String): String {
        if (CoreUtils.isEmpty(pushId)) {
            throw TransformFailureException(NO_PUSH_ID, null)
        }
        return pushId!!
    }

    companion object {
        private const val PATTERN = "pushId"
        private const val NO_PUSH_ID = "PushId is empty"
    }
}
