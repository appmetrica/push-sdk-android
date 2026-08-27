package io.appmetrica.analytics.push.impl.utils

import android.content.Context
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.IReporter
import io.appmetrica.analytics.push.BuildConfig
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.PreferenceManager
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite
import io.appmetrica.gradle.testutils.CommonTest
import io.appmetrica.gradle.testutils.data.Rand
import io.appmetrica.gradle.testutils.rules.MockedConstructionRule.Companion.constructionRule
import io.appmetrica.gradle.testutils.rules.MockedStaticRule.Companion.on
import io.appmetrica.gradle.testutils.rules.MockedStaticRule.Companion.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatcher
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class AppMetricaTrackerTest : CommonTest() {

    private var context: Context = mock()
    private val reporter: IReporter = mock()

    @get:Rule
    val appMetricaMockedStaticRule = staticRule<AppMetrica> {
        on { AppMetrica.getReporter(any(), any()) } doReturn reporter
    }

    private var controller: PushServiceControllerComposite = mock {
        on { transportIds } doReturn setOf(CoreConstants.Transport.FIREBASE, CoreConstants.Transport.UNKNOWN)
    }

    private var core: AppMetricaPushCore = mock {
        on { pushServiceController } doReturn controller
    }

    @get:Rule
    val appmetricaPushCoreStaticRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn core
    }

    private val preferenceManager: PreferenceManager = mock()
    private val nextEventId = 100500L

    @get:Rule
    val appMetricaTrackerEventIdGeneratorMockedConstructionRule = constructionRule<AppMetricaTrackerEventIdGenerator> {
        on { generate() } doReturn nextEventId
    }

    private val attributesMatcher = ArgumentMatcher<Map<String, Any?>> {
        assertThat(it).containsAllEntriesOf(
            mapOf(
                AppMetricaTracker.SDK_VERSION_CODE_FIELD to BuildConfig.VERSION_CODE.toString(),
                AppMetricaTracker.TRANSPORT_FIELD to
                    "[${CoreConstants.Transport.FIREBASE}, ${CoreConstants.Transport.UNKNOWN}]",
                AppMetricaTracker.EVENT_ID to nextEventId
            )
        )
        true
    }

    private val tracker: AppMetricaTracker by setUp { AppMetricaTracker(context, "Some api key", preferenceManager) }

    @Test
    fun appMetricaTrackerEventIdGenerator() {
        assertThat(appMetricaTrackerEventIdGeneratorMockedConstructionRule.constructionMock.constructed()).hasSize(1)
        assertThat(appMetricaTrackerEventIdGeneratorMockedConstructionRule.argumentInterceptor.flatArguments())
            .containsExactly(preferenceManager, "sdk")
    }

    @Test
    fun resumeSessionShouldSendResumeToReporter() {
        tracker.resumeSession()
        verify(reporter, Mockito.times(1)).resumeSession()
    }

    @Test
    fun pauseSessionShouldSendResumeToReporter() {
        tracker.pauseSession()
        verify(reporter, Mockito.times(1)).pauseSession()
    }

    @Test
    fun reportEventShouldSendEventNameToReporter() {
        val eventName = Rand.randomString()
        tracker.reportEvent(eventName)
        verify(reporter).reportEvent(eq(eventName), any<Map<String, Any?>>())
    }

    @Test
    fun reportEventShouldIncludeSdkVersionToEnvironment() {
        tracker.reportEvent(Rand.randomString())
        verify(reporter).reportEvent(any(), argThat(attributesMatcher))
    }

    @Test
    fun reportEventWithAttributesShouldIncludeSdkVersionToEnvironment() {
        tracker.reportEvent(Rand.randomString(), HashMap())
        verify(reporter).reportEvent(any(), argThat(attributesMatcher))
    }

    @Test
    fun reportEventWithAttributesShouldSendEventNameToReporter() {
        val eventName = Rand.randomString()
        tracker.reportEvent(eventName, null)
        verify(reporter).reportEvent(eq(eventName), anyOrNull<Map<String, Any?>>())
    }

    @Test
    fun reportEventWithAttributesShouldSendAttributesToReporter() {
        val attributes = mutableMapOf<String, Any?>(Rand.randomString() to Rand.randomString())
        tracker.reportEvent(Rand.randomString(), attributes)
        verify(reporter).reportEvent(any(), eq(attributes))
    }

    @Test
    fun reportErrorShouldSendMessageToReporter() {
        val message = Rand.randomString(10)
        tracker.reportError(message, null)
        verify(reporter).reportError(argThat { contains(message) }, anyOrNull<Throwable>())
    }

    @Test
    fun reportErrorShouldSendThrowableToReporter() {
        val throwable: Throwable = mock()
        tracker.reportError("null", throwable)
        verify(reporter).reportError(any(), eq(throwable))
    }

    @Test
    fun reportErrorShouldContainsSdkVersionInErrorMessage() {
        val message = Rand.randomString()
        tracker.reportError(message, null)
        verify(reporter).reportError(
            argThat<String> { contains("${AppMetricaTracker.SDK_VERSION_CODE_FIELD} = ${BuildConfig.VERSION_CODE}") },
            anyOrNull<Throwable>()
        )
    }

    @Test
    fun reportUnhandledExceptionShouldSendThrowableToReporter() {
        val throwable: Throwable = mock()
        tracker.reportUnhandledException(throwable)
        verify(reporter).reportUnhandledException(throwable)
    }
}
