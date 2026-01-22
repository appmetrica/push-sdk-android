package io.appmetrica.analytics.push.impl.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.notification.NotificationCustomizer
import io.appmetrica.analytics.push.notification.NotificationValueProvider
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

internal class DefaultNotificationCustomizersHolder(
    context: Context
) {

    private val customizers =
        mutableMapOf<Function<NotificationCompat.Builder>, NotificationCustomizer>()

    init {
        useListProviderFor(NotificationCompat.Builder::addAction, AdditionalActionsProvider(context))

        useProviderFor(NotificationCompat.Builder::setAutoCancel, AutoCancelProvider())
        useProviderFor(NotificationCompat.Builder::setCategory, CategoryProvider())
        useProviderFor(NotificationCompat.Builder::setChannelId, ChannelIdProvider(context))
        useProviderFor(NotificationCompat.Builder::setColor, ColorProvider())
        useProviderFor(NotificationCompat.Builder::setContentInfo, ContentInfoProvider())
        useProviderFor(NotificationCompat.Builder::setContentIntent, ContentIntentProvider(context))
        useProviderFor(NotificationCompat.Builder::setContentText, ContentTextProvider())
        useProviderFor(NotificationCompat.Builder::setContentTitle, ContentTitleProvider())
        useProviderFor(NotificationCompat.Builder::setDefaults, DefaultsProvider())
        useProviderFor(NotificationCompat.Builder::setDeleteIntent, DeleteIntentProvider(context))
        useProviderFor(NotificationCompat.Builder::setGroup, GroupProvider())
        useProviderFor(NotificationCompat.Builder::setGroupSummary, GroupSummaryProvider())
        useProviderFor(NotificationCompat.Builder::setLargeIcon, LargeIconProvider())
        useProviderFor(NotificationCompat.Builder::setLights, LightsProvider())
        useProviderFor(NotificationCompat.Builder::setNumber, NumberProvider())
        useProviderFor(NotificationCompat.Builder::setOngoing, OngoingProvider())
        useProviderFor(NotificationCompat.Builder::setOnlyAlertOnce, OnlyAlertOnceProvider())
        useProviderFor(NotificationCompat.Builder::setPriority, PriorityProvider())
        useProviderFor(NotificationCompat.Builder::setShowWhen, ShowWhenProvider())
        useProviderFor(NotificationCompat.Builder::setSmallIcon, SmallIconProvider(context))
        useProviderFor(NotificationCompat.Builder::setSortKey, SortKeyProvider())
        useProviderFor(NotificationCompat.Builder::setSound, SoundProvider())
        useProviderFor(NotificationCompat.Builder::setStyle, StyleProvider())
        useProviderFor(NotificationCompat.Builder::setSubText, SubTextProvider())
        useProviderFor(NotificationCompat.Builder::setTicker, TickerProvider())
        useProviderFor(NotificationCompat.Builder::setTimeoutAfter, TimeoutProvider(context))
        useProviderFor(NotificationCompat.Builder::setVibrate, VibrateProvider())
        useProviderFor(NotificationCompat.Builder::setVisibility, VisibilityProvider())
        useProviderFor(NotificationCompat.Builder::setWhen, WhenProvider())
    }

    fun getCustomizers(): Map<Function<NotificationCompat.Builder>, NotificationCustomizer> =
        customizers.toMap()

    private fun <T : Any> useProviderFor(
        method: NotificationCompat.Builder.(T) -> NotificationCompat.Builder,
        provider: NotificationValueProvider<T>
    ) {
        customizers[method] = NotificationCustomizer { builder: NotificationCompat.Builder, push: PushMessage ->
            NotificationBuilderMethodInvoker.invoke(method, provider, builder, push)
        }
    }

    private fun <T : Any> useProviderFor(
        method: NotificationCompat.Builder.(T, T, T) -> NotificationCompat.Builder,
        provider: NotificationValueProvider<List<T>>
    ) {
        customizers[method] = NotificationCustomizer { builder: NotificationCompat.Builder, push: PushMessage ->
            NotificationBuilderMethodInvoker.invoke(method, provider, builder, push)
        }
    }

    private fun <T : Any> useListProviderFor(
        method: NotificationCompat.Builder.(T) -> NotificationCompat.Builder,
        provider: NotificationValueProvider<List<T>>
    ) {
        customizers[method] = NotificationCustomizer { builder: NotificationCompat.Builder, push: PushMessage ->
            NotificationBuilderMethodInvoker.invokeWithList(method, provider, builder, push)
        }
    }
}
