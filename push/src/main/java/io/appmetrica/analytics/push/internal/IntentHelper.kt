package io.appmetrica.analytics.push.internal

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import io.appmetrica.analytics.push.AppMetricaPush
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.NotificationCustomizersHolderProvider
import io.appmetrica.analytics.push.impl.notification.ActivityIntentProvider
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController
import io.appmetrica.analytics.push.impl.utils.PendingIntentFlagHelper
import io.appmetrica.analytics.push.impl.utils.RequestCodeUtils
import io.appmetrica.analytics.push.impl.utils.Utils
import io.appmetrica.analytics.push.intent.NotificationActionInfo
import io.appmetrica.analytics.push.intent.NotificationActionType
import io.appmetrica.analytics.push.internal.activity.AppMetricaPushDummyActivity
import io.appmetrica.analytics.push.internal.receiver.AppMetricaPushBroadcastReceiver
import io.appmetrica.analytics.push.model.AdditionalAction
import io.appmetrica.analytics.push.model.AdditionalActionType
import io.appmetrica.analytics.push.model.OpenType
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification

object IntentHelper {

    private val activityIntentProvider: ActivityIntentProvider by lazy { ActivityIntentProvider() }

    @JvmStatic
    fun createNotificationActionInfo(
        actionType: NotificationActionType,
        pushMessage: PushMessage,
        actionUri: String?
    ): NotificationActionInfo {
        return createNotificationActionInfo(actionType, pushMessage, actionUri, null)
    }

    @JvmStatic
    fun createNotificationActionInfo(
        pushMessage: PushMessage,
        additionalAction: AdditionalAction
    ): NotificationActionInfo {
        val notificationActionType = if (additionalAction.type == AdditionalActionType.INLINE) {
            NotificationActionType.INLINE_ACTION
        } else {
            NotificationActionType.ADDITIONAL_ACTION
        }
        return createNotificationActionInfo(
            notificationActionType,
            pushMessage,
            additionalAction.actionUrl,
            additionalAction
        )
    }

    private fun createNotificationActionInfo(
        actionType: NotificationActionType,
        pushMessage: PushMessage,
        actionUri: String?,
        additionalAction: AdditionalAction?
    ): NotificationActionInfo {
        var explicitIntent = pushMessage.notification?.explicitIntent
        val builder = NotificationActionInfo.newBuilder(pushMessage.transport)
            .withPayload(pushMessage.payload)
            .withPushId(pushMessage.notificationId)
            .withActionType(actionType)
            .withTargetActionUri(actionUri)
            .withNotificationTag(pushMessage.notification?.notificationTag)
            .withNotificationId(pushMessage.notification?.notificationId ?: 0)
            .withChannelId(
                if (CoreUtils.isEmpty(pushMessage.notification?.channelId)) {
                    NotificationChannelController.DEFAULT_CHANNEL_ID
                } else {
                    pushMessage.notification?.channelId!!
                }
            )
            .withUseFlagActivityNewTask(pushMessage.notification!!.useFlagActivityNewTask)
            .also { builder ->
                NotificationCustomizersHolderProvider.customizersHolder.extraBundleProvider?.let {
                    builder.withExtraBundle(it.getExtraBundle(pushMessage))
                }
            }
        if (additionalAction != null) {
            builder.withActionId(additionalAction.id)
            builder.withUseFlagActivityNewTask(additionalAction.useFlagActivityNewTask)
            if (additionalAction.hideAfterSecond != null) {
                builder.withHideAfterSeconds(additionalAction.hideAfterSecond!!)
            }
            if (additionalAction.hideQuickControlPanel != null) {
                builder.withHideQuickControlPanel(additionalAction.hideQuickControlPanel!!)
            }
            if (additionalAction.autoCancel != null) {
                builder.withDismissOnAdditionalAction(additionalAction.autoCancel!!)
            }
            if (additionalAction.type != null) {
                if (additionalAction.type == AdditionalActionType.OPEN_APP_URI) {
                    explicitIntent = true
                }
                if (additionalAction.type == AdditionalActionType.DO_NOTHING) {
                    builder.withDoNothing(true)
                }
            } else {
                explicitIntent = additionalAction.explicitIntent
            }
        }
        builder.withExplicitIntent(explicitIntent != null && explicitIntent)
        return builder.build()
    }

    @JvmStatic
    fun createWrappedAction(
        context: Context,
        actionInfo: NotificationActionInfo,
        autoTracking: Boolean
    ): PendingIntent {
        var intent: Intent? = null
        if (!autoTracking) {
            intent = activityIntentProvider.getIntentForActionOrDefaultLaunch(context, actionInfo.targetActionUri)
        }
        if (intent == null) {
            intent = Intent(AppMetricaPushBroadcastReceiver.ACTION_BROADCAST_ACTION)
            intent.putExtra(AppMetricaPush.EXTRA_ACTION_INFO, actionInfo)
            intent.setPackage(context.packageName)
            if (actionInfo.useFlagActivityNewTask) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } else {
            intent.putExtra(AppMetricaPush.EXTRA_ACTION_INFO, actionInfo)
            if (actionInfo.extraBundle != null) {
                intent.putExtras(actionInfo.extraBundle)
            }
            if (actionInfo.explicitIntent) {
                intent.setPackage(context.packageName)
            }
            intent.putExtra(AppMetricaPush.EXTRA_PAYLOAD, actionInfo.payload)
        }

        val requestCode = RequestCodeUtils.incrementAndGet(context)
        val needMutableFlag = actionInfo.actionType == NotificationActionType.INLINE_ACTION
        val pendingIntentFlag =
            PendingIntentFlagHelper.getPendingIntentFlag(PendingIntent.FLAG_CANCEL_CURRENT, needMutableFlag)

        return if (autoTracking) {
            PendingIntent.getBroadcast(context, requestCode, intent, pendingIntentFlag)
        } else {
            PendingIntent.getActivity(context, requestCode, intent, pendingIntentFlag)
        }
    }

    @JvmStatic
    fun getPendingIntentForOpenAction(
        context: Context,
        pushNotification: PushNotification?,
        actionInfo: NotificationActionInfo
    ): PendingIntent {
        return when (wrapOpenType(pushNotification?.openType ?: OpenType.UNKNOWN, actionInfo)) {
            OpenType.APPLICATION_ACTIVITY ->
                createWrappedAction(context, actionInfo, false)
            OpenType.TRANSPARENT_ACTIVITY ->
                createWrappedActionActivity(context, actionInfo)
            else -> {
                val autoTracking = AppMetricaPushCore.getInstance(context)
                    .pushServiceProvider
                    .autoTrackingConfiguration
                    .trackingOpenAction
                createWrappedAction(context, actionInfo, autoTracking)
            }
        }
    }

    @JvmStatic
    fun getPendingIntentForAdditionalAction(
        context: Context,
        additionalAction: AdditionalAction,
        actionInfo: NotificationActionInfo
    ): PendingIntent {
        return when (wrapOpenType(additionalAction.openType, actionInfo)) {
            OpenType.APPLICATION_ACTIVITY ->
                createWrappedAction(context, actionInfo, false)
            OpenType.TRANSPARENT_ACTIVITY ->
                createWrappedActionActivity(context, actionInfo)
            else -> {
                val autoTracking = AppMetricaPushCore.getInstance(context)
                    .pushServiceProvider
                    .autoTrackingConfiguration
                    .isTrackingAdditionalAction(additionalAction.id)
                createWrappedAction(context, actionInfo, autoTracking)
            }
        }
    }

    private fun wrapOpenType(
        openType: OpenType,
        info: NotificationActionInfo
    ): OpenType {
        return if (openType == OpenType.UNKNOWN) {
            if (Utils.isApiAchived(Build.VERSION_CODES.S)) {
                if (info.doNothing) {
                    OpenType.BROADCAST
                } else {
                    OpenType.TRANSPARENT_ACTIVITY
                }
            } else {
                OpenType.BROADCAST
            }
        } else {
            openType
        }
    }

    private fun createWrappedActionActivity(
        context: Context,
        actionInfo: NotificationActionInfo
    ): PendingIntent {
        val intent = Intent(context, AppMetricaPushDummyActivity::class.java).apply {
            putExtra(AppMetricaPush.EXTRA_ACTION_INFO, actionInfo)
            setPackage(context.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        return PendingIntent.getActivity(
            context,
            RequestCodeUtils.incrementAndGet(context),
            intent,
            PendingIntentFlagHelper.getPendingIntentFlag(
                PendingIntent.FLAG_CANCEL_CURRENT,
                actionInfo.actionType == NotificationActionType.INLINE_ACTION
            )
        )
    }
}
