package io.appmetrica.analytics.push.impl.tracking;

import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.ModuleEvent;
import io.appmetrica.analytics.ModulesFacade;
import io.appmetrica.analytics.push.BuildConfig;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import java.util.Map;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaPushMessageTrackerTests {

    private static final String JSON_NOTIFICATION_ID = "notification_id";
    private static final String JSON_ACTION = "action";
    private static final String JSON_ACTION_TYPE = "type";
    private static final String JSON_ACTION_ID = "id";
    private static final String JSON_ACTION_CATEGORY = "category";
    private static final String JSON_ACTION_DETAILS = "details";
    private static final String JSON_ACTION_TEXT = "text";
    private static final String JSON_NEW_PUSH_ID = "new_push_id";

    private static final String ACTION_TYPE_CLEAR = "dismiss";
    private static final String ACTION_TYPE_OPEN = "open";
    private static final String ACTION_TYPE_RECEIVE = "receive";
    private static final String ACTION_TYPE_CUSTOM = "custom";
    private static final String ACTION_TYPE_PROCESSED = "processed";
    private static final String ACTION_TYPE_SHOWN = "shown";
    private static final String ACTION_TYPE_IGNORED = "ignored";
    private static final String ACTION_TYPE_EXPIRED = "expired";
    private static final String ACTION_TYPE_REMOVED = "removed";
    private static final String ACTION_TYPE_REPLACE = "replace";

    private static final String TRANSPORT = "transport";

    private int mEventType;
    private String mEventName;
    private String mEventValue;
    private Map<String, Object> mEnvironment;

    @Rule
    public final MockedStaticRule<AppMetrica> appMetricaRule = new MockedStaticRule<>(AppMetrica.class);
    @Rule
    public final MockedStaticRule<ModulesFacade> modulesFacadeRule = new MockedStaticRule<>(ModulesFacade.class);
    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    private AppMetricaPushMessageTracker mTracker;
    private TrackersHub mTrackersHub;

    @Before
    public void setUp() {
        mTracker = new AppMetricaPushMessageTracker();
        mEventType = Integer.MIN_VALUE;
        mEventName = null;
        mEventValue = null;
        mEnvironment = null;

        mTrackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(mTrackersHub);
    }

    // region init token
    @Test
    public void testOnInitPushTokenShouldSendEventWithExpectedType() throws Exception {
        mTracker.onPushTokenInited(randomString(), TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_PUSH_TOKEN.getId());
    }

    @Test
    public void testOnInitPushTokenShouldSendEventWithExpectedName() throws Exception {
        mTracker.onPushTokenInited(randomString(), TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_PUSH_TOKEN.getCaption());
    }

    @Test
    public void testOnInitPushTokenShouldSendEventWithExpectedValue() throws Exception {
        String pushToken = randomString();
        mTracker.onPushTokenInited(pushToken, TRANSPORT);
        checkBridge();
        assertThat(mEventValue).isEqualTo(pushToken);
    }

    @Test
    public void testOnInitPushTokenShouldSendEventWithExpectedEnvironment() throws Exception {
        mTracker.onPushTokenInited(randomString(), TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region update token
    @Test
    public void testOnUpdatePushTokenShouldSendEventWithExpectedType() {
        mTracker.onPushTokenUpdated(randomString(100), TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_PUSH_TOKEN.getId());
    }

    @Test
    public void testOnUpdatePushTokenShouldSendEventWithExpectedName() {
        mTracker.onPushTokenUpdated(randomString(100), TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_PUSH_TOKEN.getCaption());
    }

    @Test
    public void testOnUpdatePushTokenShouldSendEventWithExpectedValue() {
        String expectedPushToken = randomString();
        mTracker.onPushTokenUpdated(expectedPushToken, TRANSPORT);
        checkBridge();
        assertThat(mEventValue).isEqualTo(expectedPushToken);
    }

    @Test
    public void testOnUpdatePushTokenShouldSendEventWithExpectedEnvironment() {
        mTracker.onPushTokenUpdated(randomString(), TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region receive
    @Test
    public void testOnReceiveMessageShouldSendEventWithExpectedType() {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnReceiveMessageShouldSendEventWithExpectedName() {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnReceiveMessageShouldSendEventWithExpectedPushIdInValue() throws Exception {
        String expectedPushId = randomString();
        mTracker.onMessageReceived(expectedPushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void testOnReceiveMessageShouldSendEventWithExpectedActionTypeInValue() throws Exception {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_RECEIVE);
    }

    @Test
    public void testOnReceiveMessageShouldSendEventWithEventWithoutActionIdInValue() throws Exception {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).has(JSON_ACTION_ID)).isFalse();
    }

    @Test
    public void testOnReceiveMessageShouldSendEventWithExpectedEnvironment() {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region clear
    @Test
    public void testOnClearNotificationShouldSendEventWithExpectedType() {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnClearNotificationShouldSendEventWithExpectedName() {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnClearNotificationShouldSendEventWithExpectedPushIdInValue() throws Exception {
        String pushId = randomString();
        mTracker.onNotificationCleared(pushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(pushId);
    }

    @Test
    public void testOnClearNotificationShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_CLEAR);
    }

    @Test
    public void testOnClearNotificationShouldSendEventWithoutActionId() throws Exception {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).has(JSON_ACTION_ID)).isFalse();
    }

    @Test
    public void testOnClearNotificationShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region open
    @Test
    public void testOnOpenNotificationShouldSendEventWithExpectedTypeId() {
        mTracker.onPushOpened(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnOpenPushNotificationShouldSendEventWithExpectedName() {
        mTracker.onPushOpened(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnOpenPushNotificationShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onPushOpened(expectedPushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void testOnOpenPushNotificationShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onPushOpened(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_OPEN);
    }

    @Test
    public void testOnOpenPushNotificationShouldSendEventWithoutActionId() throws Exception {
        mTracker.onPushOpened(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).has(JSON_ACTION_ID)).isFalse();
    }

    @Test
    public void testOnOpenPushNotificationShouldSendEventWithExpectedEnvironment() throws Exception {
        mTracker.onPushOpened(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region additional action
    @Test
    public void testOnNotificationAdditionalActionShouldSendEventWithExpectedType() {
        mTracker.onNotificationAdditionalAction(randomString(),
            randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnNotificationAdditionalActionShouldSendEventWithExpectedName() {
        mTracker.onNotificationAdditionalAction(randomString(),
            randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnNotificationAdditionalActionShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onNotificationAdditionalAction(
            expectedPushId,
            randomString(),
            null,
            TRANSPORT
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void testOnNotificationAdditionalActionShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationAdditionalAction(randomString(),
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_CUSTOM);
    }

    @Test
    public void testOnNotificationAdditionalActionShouldSendEventWithExpectedActionId() throws Exception {
        String expectedActionId = randomString();
        mTracker.onNotificationAdditionalAction(
            randomString(),
            expectedActionId,
            null,
            TRANSPORT
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_ID)).isEqualTo(expectedActionId);
    }

    @Test
    public void testOnNotificationAdditionalActionShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationAdditionalAction(randomString(),
            randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region inline additional action
    @Test
    public void testOnNotificationInlineAdditionalActionShouldSendEventWithExpectedType() {
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT
        );
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnNotificationInlineAdditionalActionShouldSendEventWithExpectedName() {
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT
        );
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnNotificationInlineAdditionalActionShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_CUSTOM);
    }

    @Test
    public void testOnNotificationInlineAdditionalActionShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT
        );
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }

    @Test
    public void testOnNotificationInlineAdditionalActionShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onNotificationInlineAdditionalAction(
            expectedPushId,
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void testOnNotificationInlineAdditionalActionShouldSendEventWithExpectedActionId() throws Exception {
        String expectedActionId = randomString();
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            expectedActionId,
            randomString(),
            randomString(),
            TRANSPORT
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_ID)).isEqualTo(expectedActionId);
    }

    @Test
    public void testOnNotificationInlineAdditionalActionShouldSendEventWithExpectedText() throws Exception {
        String expectedText = randomString();
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            expectedText,
            TRANSPORT
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TEXT)).isEqualTo(expectedText);
    }
    // endregion

    // region process silent
    @Test
    public void testOnProcessSilentPushShouldSendEventWithExpectedTypeId() {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnProcessSilentPushShouldSendEventWithExpectedName() {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnProcessSilentPushShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onSilentPushProcessed(expectedPushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void testOnProcessSilentPushShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_PROCESSED);
    }

    @Test
    public void testOnProcessSilentPushShouldSendEventWithoutActionId() throws Exception {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).has(JSON_ACTION_ID)).isFalse();
    }

    @Test
    public void testOnProcessSilentPushShouldSendEventWithExpectedEnvironment() {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region shown
    @Test
    public void testOnNotificationShownShouldSendEventWithExpectedTypeId() {
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnNotificationShownShouldSendEventWithExpectedName() {
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnNotificationShownShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onNotificationShown(expectedPushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void testOnNotificationShownShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_SHOWN);
    }

    @Test
    public void testOnNotificationShownShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region ignored
    @Test
    public void testOnNotificationIgnoredShouldSendEventWithExpectedType() {
        mTracker.onNotificationIgnored(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnNotificationIgnoredShouldSendEventWithExpectedName() {
        mTracker.onNotificationIgnored(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnNotificationIgnoredShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onNotificationIgnored(expectedPushId, randomString(),
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void testOnNotificationIgnoredShouldSendEventWithExpectedCategory() throws Exception {
        String expectedCategory = randomString();
        mTracker.onNotificationIgnored(randomString(), expectedCategory,
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_CATEGORY)).isEqualTo(expectedCategory);
    }

    @Test
    public void testOnNotificationIgnoredShouldSendEventWithExpectedDetails() throws Exception {
        String expectedDetails = randomString();
        mTracker.onNotificationIgnored(randomString(),
            randomString(), expectedDetails, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_DETAILS)).isEqualTo(expectedDetails);
    }

    @Test
    public void testOnNotificationIgnoredShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationIgnored(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_IGNORED);
    }

    @Test
    public void testOnNotificationIgnoredShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationIgnored(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region expired
    @Test
    public void testOnNotificationExpiredShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        String expectedCategory = randomString();
        mTracker.onNotificationExpired(expectedPushId, expectedCategory, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_CATEGORY)).isEqualTo(expectedCategory);
    }

    @Test
    public void testOnNotificationDismissByTtlShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationExpired(
            randomString(),
            randomString(),
            null,
            TRANSPORT
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_EXPIRED);
    }

    @Test
    public void testOnNotificationDismissByTtlShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationExpired(
            randomString(),
            randomString(),
            null,
            TRANSPORT
        );
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region silent remove
    @Test
    public void testOnRemovingSilentPushProcessedShouldSendEventWithExpectedType() {
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void testOnRemovingSilentPushProcessedShouldSendEventWithExpectedName() {
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void testOnRemovingSilentPushProcessedShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onRemovingSilentPushProcessed(expectedPushId, randomString(),
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void testOnRemovingSilentPushProcessedShouldSendEventWithExpectedCategory() throws Exception {
        String expectedCategory = randomString();
        mTracker.onRemovingSilentPushProcessed(randomString(), expectedCategory,
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_CATEGORY)).isEqualTo(expectedCategory);
    }

    @Test
    public void testOnRemovingSilentPushProcessedShouldSendEventWithExpectedDetails() throws Exception {
        String expectedDetails = randomString();
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), expectedDetails, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_DETAILS)).isEqualTo(expectedDetails);
    }

    @Test
    public void testOnRemovingSilentPushProcessedShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_REMOVED);
    }

    @Test
    public void testOnRemovingSilentPushProcessedShouldSendEventWithExpectedEnvironment() {
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    // region replace
    @Test
    public void testOnNotificationReplaceShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        String expectedNewPushId = randomString();
        mTracker.onNotificationReplace(expectedPushId, expectedNewPushId, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_NEW_PUSH_ID)).isEqualTo(expectedNewPushId);
    }

    @Test
    public void testOnNotificationReplaceShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationReplace(randomString(), randomString(), TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_REPLACE);
    }

    @Test
    public void testOnNotificationReplaceShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationReplace(randomString(), randomString(), TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }
    // endregion

    private void checkBridge() {
        final ArgumentCaptor<ModuleEvent> moduleEventArgumentCaptor = ArgumentCaptor.forClass(ModuleEvent.class);

        modulesFacadeRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                ModulesFacade.reportEvent(
                    moduleEventArgumentCaptor.capture()
                );
            }
        });
        appMetricaRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                AppMetrica.sendEventsBuffer();
            }
        });

        final ModuleEvent event = moduleEventArgumentCaptor.getValue();
        mEventType = event.getType();
        mEventName = event.getName();
        mEventValue = event.getValue();
        mEnvironment = event.getEnvironment();
    }

    @Test
    public void testOnInitPushTokenShouldWorkCorrectlyIfAppMetricaHasNotActivatedYet() throws Exception {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onPushTokenInited(randomString(), TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnUpdatePushTokenShouldWorkCorrectlyIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onPushTokenUpdated(randomString(), TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnReceiveMessageShouldWorkCorrectlyIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnClearNotificationShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnOpenNotificationShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onPushOpened(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnNotificationAdditionalActionShouldSendEventToBridgeOfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationAdditionalAction(
            randomString(),
            randomString(),
            null,
            TRANSPORT
        );
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnNotificationInlineAdditionalActionShouldSendEventToBridgeOfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            null,
            randomString(),
            TRANSPORT
        );
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnProcessSilentPushShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnNotificationShownShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnNotificationIgnoredShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationIgnored(
            randomString(),
            randomString(),
            randomString(),
            null,
            TRANSPORT
        );
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnNotificationTtlShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationExpired(
            randomString(),
            randomString(),
            null,
            TRANSPORT
        );
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnRemovingSilentPushProcessedShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onRemovingSilentPushProcessed(randomString(), null, null, null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testOnNotificationReplaceShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationReplace(randomString(), randomString(), TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }
}
