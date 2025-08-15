package io.appmetrica.analytics.push.internal

import android.os.Bundle
import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.impl.NotificationCustomizersHolderProvider
import io.appmetrica.analytics.push.intent.NotificationActionType
import io.appmetrica.analytics.push.model.AdditionalAction
import io.appmetrica.analytics.push.model.AdditionalActionType
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.notification.ExtraBundleProvider
import io.appmetrica.analytics.push.notification.NotificationCustomizersHolder
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomInt
import io.appmetrica.analytics.push.testutils.Rand.randomLong
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class IntentHelperTest : CommonTest() {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()
    private val additionalAction: AdditionalAction = mock()

    private val notificationId = randomInt()
    private val channelId = randomString()
    private val notificationTag = randomString()
    private val pushId = randomString()
    private val transport = randomString()
    private val payload = randomString()
    private val useFlagActivityNewTask = true

    @get:Rule
    val notificationFactoryCustomizerProviderRule =
        MockedStaticRule(NotificationCustomizersHolderProvider::class.java)
    private val notificationCustomizersHolder: NotificationCustomizersHolder = mock()

    @Before
    fun setUp() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(pushMessage.notificationId).thenReturn(pushId)
        whenever(pushMessage.transport).thenReturn(transport)
        whenever(pushMessage.payload).thenReturn(payload)

        whenever(notification.notificationId).thenReturn(notificationId)
        whenever(notification.channelId).thenReturn(channelId)
        whenever(notification.notificationTag).thenReturn(notificationTag)
        whenever(notification.useFlagActivityNewTask).thenReturn(useFlagActivityNewTask)

        whenever(NotificationCustomizersHolderProvider.customizersHolder).thenReturn(notificationCustomizersHolder)
    }

    @Test
    fun createNotificationActionInfoWithActionUri() {
        val explicitIntent = true
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        val actionType = NotificationActionType.CLEAR
        val actionUri = randomString()

        val actionInfo = IntentHelper.createNotificationActionInfo(
            actionType,
            pushMessage,
            actionUri
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("targetActionUri", actionUri)
            .checkField("payload", payload)
            .checkField("actionType", actionType)
            .checkFieldIsNull("actionId")
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", 0L)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", false)
            .checkField("dismissOnAdditionalAction", false)
            .checkFieldIsNull("extraBundle")
            .checkField("explicitIntent", explicitIntent)
            .checkField("doNothing", false)
            .checkField("useFlagActivityNewTask", useFlagActivityNewTask)
            .checkAll()
    }

    @Test
    fun createNotificationActionInfoWithActionUriAndExtraBundle() {
        val explicitIntent = true
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        val extraBundleProvider: ExtraBundleProvider = mock()
        val extraBundle = Bundle().apply {
            putString("key", "value")
        }
        whenever(extraBundleProvider.getExtraBundle(pushMessage)).thenReturn(extraBundle)
        whenever(notificationCustomizersHolder.extraBundleProvider).thenReturn(extraBundleProvider)

        val actionType = NotificationActionType.CLEAR
        val actionUri = randomString()

        val actionInfo = IntentHelper.createNotificationActionInfo(
            actionType,
            pushMessage,
            actionUri
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("targetActionUri", actionUri)
            .checkField("payload", payload)
            .checkField("actionType", actionType)
            .checkFieldIsNull("actionId")
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", 0L)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", false)
            .checkField("dismissOnAdditionalAction", false)
            .checkFieldComparingFieldByField("extraBundle", extraBundle)
            .checkField("explicitIntent", explicitIntent)
            .checkField("doNothing", false)
            .checkField("useFlagActivityNewTask", useFlagActivityNewTask)
            .checkAll()
    }

    @Test
    fun createNotificationActionInfoWithActionUriIfActionUriIsNull() {
        val explicitIntent = true
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        val actionType = NotificationActionType.CLEAR

        val actionInfo = IntentHelper.createNotificationActionInfo(
            actionType,
            pushMessage,
            null
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkFieldIsNull("targetActionUri")
            .checkField("payload", payload)
            .checkField("actionType", actionType)
            .checkFieldIsNull("actionId")
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", 0L)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", false)
            .checkField("dismissOnAdditionalAction", false)
            .checkFieldIsNull("extraBundle")
            .checkField("explicitIntent", explicitIntent)
            .checkField("doNothing", false)
            .checkField("useFlagActivityNewTask", useFlagActivityNewTask)
            .checkAll()
    }

    @Test
    fun createNotificationActionInfoWithActionUriIfExplicitIntentIsFalse() {
        val explicitIntent = false
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        val actionType = NotificationActionType.CLEAR

        val actionInfo = IntentHelper.createNotificationActionInfo(
            actionType,
            pushMessage,
            null
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkFieldIsNull("targetActionUri")
            .checkField("payload", payload)
            .checkField("actionType", actionType)
            .checkFieldIsNull("actionId")
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", 0L)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", false)
            .checkField("dismissOnAdditionalAction", false)
            .checkFieldIsNull("extraBundle")
            .checkField("explicitIntent", explicitIntent)
            .checkField("doNothing", false)
            .checkField("useFlagActivityNewTask", useFlagActivityNewTask)
            .checkAll()
    }

    @Test
    fun createNotificationActionInfoWithAdditionalAction() {
        val explicitIntent = true
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        val additionalActionType = AdditionalActionType.INLINE
        whenever(additionalAction.id).thenReturn(randomString())
        whenever(additionalAction.type).thenReturn(additionalActionType)
        whenever(additionalAction.useFlagActivityNewTask).thenReturn(!useFlagActivityNewTask)

        val actionInfo = IntentHelper.createNotificationActionInfo(
            pushMessage,
            additionalAction
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkFieldIsNull("targetActionUri")
            .checkField("payload", payload)
            .checkField("actionType", NotificationActionType.INLINE_ACTION)
            .checkField("actionId", additionalAction.id)
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", 0L)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", false)
            .checkField("dismissOnAdditionalAction", false)
            .checkFieldIsNull("extraBundle")
            .checkField("explicitIntent", explicitIntent)
            .checkField("doNothing", false)
            .checkField("useFlagActivityNewTask", additionalAction.useFlagActivityNewTask)
            .checkAll()
    }

    @Test
    fun createNotificationActionInfoWithAdditionalActionIfTypeIsOpenAppUri() {
        val explicitIntent = false
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        val additionalActionType = AdditionalActionType.OPEN_APP_URI
        whenever(additionalAction.id).thenReturn(randomString())
        whenever(additionalAction.type).thenReturn(additionalActionType)
        whenever(additionalAction.useFlagActivityNewTask).thenReturn(!useFlagActivityNewTask)

        val actionInfo = IntentHelper.createNotificationActionInfo(
            pushMessage,
            additionalAction
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkFieldIsNull("targetActionUri")
            .checkField("payload", payload)
            .checkField("actionType", NotificationActionType.ADDITIONAL_ACTION)
            .checkField("actionId", additionalAction.id)
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", 0L)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", false)
            .checkField("dismissOnAdditionalAction", false)
            .checkFieldIsNull("extraBundle")
            .checkField("explicitIntent", true)
            .checkField("doNothing", false)
            .checkField("useFlagActivityNewTask", additionalAction.useFlagActivityNewTask)
            .checkAll()
    }

    @Test
    fun createNotificationActionInfoWithAdditionalActionIfTypeIsDoNothing() {
        val explicitIntent = true
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        val additionalActionType = AdditionalActionType.DO_NOTHING
        whenever(additionalAction.id).thenReturn(randomString())
        whenever(additionalAction.type).thenReturn(additionalActionType)
        whenever(additionalAction.useFlagActivityNewTask).thenReturn(!useFlagActivityNewTask)

        val actionInfo = IntentHelper.createNotificationActionInfo(
            pushMessage,
            additionalAction
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkFieldIsNull("targetActionUri")
            .checkField("payload", payload)
            .checkField("actionType", NotificationActionType.ADDITIONAL_ACTION)
            .checkField("actionId", additionalAction.id)
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", 0L)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", false)
            .checkField("dismissOnAdditionalAction", false)
            .checkFieldIsNull("extraBundle")
            .checkField("explicitIntent", true)
            .checkField("doNothing", true)
            .checkField("useFlagActivityNewTask", additionalAction.useFlagActivityNewTask)
            .checkAll()
    }

    @Test
    fun createNotificationActionInfoWithAdditionalActionIfTypeIsNull() {
        val explicitIntent = true
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        whenever(additionalAction.id).thenReturn(randomString())
        whenever(additionalAction.type).thenReturn(null)
        whenever(additionalAction.explicitIntent).thenReturn(false)
        whenever(additionalAction.useFlagActivityNewTask).thenReturn(!useFlagActivityNewTask)

        val actionInfo = IntentHelper.createNotificationActionInfo(
            pushMessage,
            additionalAction
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkFieldIsNull("targetActionUri")
            .checkField("payload", payload)
            .checkField("actionType", NotificationActionType.ADDITIONAL_ACTION)
            .checkField("actionId", additionalAction.id)
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", 0L)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", false)
            .checkField("dismissOnAdditionalAction", false)
            .checkFieldIsNull("extraBundle")
            .checkField("explicitIntent", false)
            .checkField("doNothing", false)
            .checkField("useFlagActivityNewTask", additionalAction.useFlagActivityNewTask)
            .checkAll()
    }

    @Test
    fun createNotificationActionInfoWithAdditionalActionWithAllFields() {
        val explicitIntent = true
        whenever(notification.explicitIntent).thenReturn(explicitIntent)

        val additionalActionType = AdditionalActionType.INLINE
        whenever(additionalAction.id).thenReturn(randomString())
        whenever(additionalAction.type).thenReturn(additionalActionType)
        whenever(additionalAction.useFlagActivityNewTask).thenReturn(!useFlagActivityNewTask)
        whenever(additionalAction.hideAfterSecond).thenReturn(randomLong())
        whenever(additionalAction.hideQuickControlPanel).thenReturn(true)
        whenever(additionalAction.autoCancel).thenReturn(true)

        val actionInfo = IntentHelper.createNotificationActionInfo(
            pushMessage,
            additionalAction
        )
        ObjectPropertyAssertions(actionInfo)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkFieldIsNull("targetActionUri")
            .checkField("payload", payload)
            .checkField("actionType", NotificationActionType.INLINE_ACTION)
            .checkField("actionId", additionalAction.id)
            .checkField("notificationTag", notificationTag)
            .checkField("notificationId", notificationId)
            .checkField("hideAfterSeconds", additionalAction.hideAfterSecond)
            .checkField("channelId", channelId)
            .checkField("hideQuickControlPanel", additionalAction.hideQuickControlPanel)
            .checkField("dismissOnAdditionalAction", additionalAction.autoCancel)
            .checkFieldIsNull("extraBundle")
            .checkField("explicitIntent", explicitIntent)
            .checkField("doNothing", false)
            .checkField("useFlagActivityNewTask", additionalAction.useFlagActivityNewTask)
            .checkAll()
    }
}
