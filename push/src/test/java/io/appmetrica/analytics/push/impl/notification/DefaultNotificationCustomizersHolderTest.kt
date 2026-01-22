package io.appmetrica.analytics.push.impl.notification

import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import io.appmetrica.analytics.push.notification.providers.AdditionalActionsProvider
import io.appmetrica.analytics.push.notification.providers.AutoCancelProvider
import io.appmetrica.analytics.push.notification.providers.CategoryProvider
import io.appmetrica.analytics.push.notification.providers.ChannelIdProvider
import io.appmetrica.analytics.push.notification.providers.ColorProvider
import io.appmetrica.analytics.push.notification.providers.ContentInfoProvider
import io.appmetrica.analytics.push.notification.providers.ContentIntentProvider
import io.appmetrica.analytics.push.notification.providers.ContentTextProvider
import io.appmetrica.analytics.push.notification.providers.ContentTitleProvider
import io.appmetrica.analytics.push.notification.providers.DefaultsProvider
import io.appmetrica.analytics.push.notification.providers.DeleteIntentProvider
import io.appmetrica.analytics.push.notification.providers.GroupProvider
import io.appmetrica.analytics.push.notification.providers.GroupSummaryProvider
import io.appmetrica.analytics.push.notification.providers.LargeIconProvider
import io.appmetrica.analytics.push.notification.providers.LightsProvider
import io.appmetrica.analytics.push.notification.providers.NumberProvider
import io.appmetrica.analytics.push.notification.providers.OngoingProvider
import io.appmetrica.analytics.push.notification.providers.OnlyAlertOnceProvider
import io.appmetrica.analytics.push.notification.providers.PriorityProvider
import io.appmetrica.analytics.push.notification.providers.ShowWhenProvider
import io.appmetrica.analytics.push.notification.providers.SmallIconProvider
import io.appmetrica.analytics.push.notification.providers.SortKeyProvider
import io.appmetrica.analytics.push.notification.providers.SoundProvider
import io.appmetrica.analytics.push.notification.providers.StyleProvider
import io.appmetrica.analytics.push.notification.providers.SubTextProvider
import io.appmetrica.analytics.push.notification.providers.TickerProvider
import io.appmetrica.analytics.push.notification.providers.TimeoutProvider
import io.appmetrica.analytics.push.notification.providers.VibrateProvider
import io.appmetrica.analytics.push.notification.providers.VisibilityProvider
import io.appmetrica.analytics.push.notification.providers.WhenProvider
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

private typealias NotificationBuilderMethod<T> = NotificationCompat.Builder.(T) -> NotificationCompat.Builder

internal class DefaultNotificationCustomizersHolderTest : CommonTest() {

    private val context: Context = mock()

    private val addActionMethod: NotificationBuilderMethod<NotificationCompat.Action> =
        NotificationCompat.Builder::addAction
    private val setSmallIconMethod: NotificationBuilderMethod<Int> = NotificationCompat.Builder::setSmallIcon
    private val setSoundMethod: NotificationBuilderMethod<Uri> = NotificationCompat.Builder::setSound
    private val setTickerMethod: NotificationBuilderMethod<CharSequence> = NotificationCompat.Builder::setTicker

    private val oneVariableMethods = mapOf(
        NotificationCompat.Builder::setAutoCancel to AutoCancelProvider(),
        NotificationCompat.Builder::setCategory to CategoryProvider(),
        NotificationCompat.Builder::setChannelId to ChannelIdProvider(context),
        NotificationCompat.Builder::setColor to ColorProvider(),
        NotificationCompat.Builder::setContentInfo to ContentInfoProvider(),
        NotificationCompat.Builder::setContentIntent to ContentIntentProvider(context),
        NotificationCompat.Builder::setContentText to ContentTextProvider(),
        NotificationCompat.Builder::setContentTitle to ContentTitleProvider(),
        NotificationCompat.Builder::setDefaults to DefaultsProvider(),
        NotificationCompat.Builder::setDeleteIntent to DeleteIntentProvider(context),
        NotificationCompat.Builder::setGroup to GroupProvider(),
        NotificationCompat.Builder::setGroupSummary to GroupSummaryProvider(),
        NotificationCompat.Builder::setLargeIcon to LargeIconProvider(),
        NotificationCompat.Builder::setNumber to NumberProvider(),
        NotificationCompat.Builder::setOngoing to OngoingProvider(),
        NotificationCompat.Builder::setOnlyAlertOnce to OnlyAlertOnceProvider(),
        NotificationCompat.Builder::setPriority to PriorityProvider(),
        NotificationCompat.Builder::setShowWhen to ShowWhenProvider(),
        setSmallIconMethod to SmallIconProvider(context),
        NotificationCompat.Builder::setSortKey to SortKeyProvider(),
        setSoundMethod to SoundProvider(),
        NotificationCompat.Builder::setStyle to StyleProvider(),
        NotificationCompat.Builder::setSubText to SubTextProvider(),
        setTickerMethod to TickerProvider(),
        NotificationCompat.Builder::setTimeoutAfter to TimeoutProvider(context),
        NotificationCompat.Builder::setVibrate to VibrateProvider(),
        NotificationCompat.Builder::setVisibility to VisibilityProvider(),
        NotificationCompat.Builder::setWhen to WhenProvider()
    )
    private val threeVariableMethods = mapOf(
        NotificationCompat.Builder::setLights to LightsProvider(),
    )
    private val listMethods = mapOf(
        addActionMethod to AdditionalActionsProvider(context),
    )
    private val allMethods = oneVariableMethods + threeVariableMethods + listMethods

    private val providers = DefaultNotificationCustomizersHolder(context).getCustomizers()

    @Test
    fun checkGetDefaultExtensionsKeys() {
        assertThat(providers.keys).containsExactlyInAnyOrderElementsOf(allMethods.keys)
    }
}
