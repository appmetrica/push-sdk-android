package io.appmetrica.analytics.push.impl.tracking;

import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.ModuleEvent;
import io.appmetrica.analytics.ModulesFacade;
import io.appmetrica.analytics.push.BuildConfig;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.utils.AppMetricaTrackerEventIdGenerator;
import io.appmetrica.analytics.push.testutils.CommonTest;
import io.appmetrica.analytics.push.testutils.MockedConstructionRule;
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
public class AppMetricaPushMessageTrackerTests extends CommonTest {

    private static final String JSON_NOTIFICATION_ID = "notification_id";
    private static final String JSON_ACTION = "action";
    private static final String JSON_ACTION_TYPE = "type";
    private static final String JSON_ACTION_ID = "id";
    private static final String JSON_ACTION_URI = "uri";
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
    private PreferenceManager preferenceManager;
    private long eventId = 42L;

    @Rule
    public final MockedStaticRule<AppMetrica> appMetricaRule = new MockedStaticRule<>(AppMetrica.class);
    @Rule
    public final MockedStaticRule<ModulesFacade> modulesFacadeRule = new MockedStaticRule<>(ModulesFacade.class);
    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);
    @Rule
    public final MockedConstructionRule<AppMetricaTrackerEventIdGenerator> eventIdGeneratorMockedConstructionRule =
        new MockedConstructionRule<>(
            AppMetricaTrackerEventIdGenerator.class,
            (mock, context) -> when(mock.generate()).thenReturn(eventId)
        );

    private AppMetricaPushMessageTracker mTracker;
    private TrackersHub mTrackersHub;

    @Before
    public void setUp() {
        preferenceManager = mock(PreferenceManager.class);
        mTracker = new AppMetricaPushMessageTracker(preferenceManager);
        mEventType = Integer.MIN_VALUE;
        mEventName = null;
        mEventValue = null;
        mEnvironment = null;

        mTrackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(mTrackersHub);
    }

    @Test
    public void eventIdGenerator() {
        assertThat(eventIdGeneratorMockedConstructionRule.getConstructionMock().constructed()).hasSize(1);
        assertThat(eventIdGeneratorMockedConstructionRule.getArgumentInterceptor().flatArguments())
            .containsExactly(preferenceManager, "app");
    }

    // region init token
    @Test
    public void onInitPushTokenShouldSendEventWithExpectedType() throws Exception {
        mTracker.onPushTokenInited(randomString(), TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_PUSH_TOKEN.getId());
    }

    @Test
    public void onInitPushTokenShouldSendEventWithExpectedName() throws Exception {
        mTracker.onPushTokenInited(randomString(), TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_PUSH_TOKEN.getCaption());
    }

    @Test
    public void onInitPushTokenShouldSendEventWithExpectedValue() throws Exception {
        String pushToken = randomString();
        mTracker.onPushTokenInited(pushToken, TRANSPORT);
        checkBridge();
        assertThat(mEventValue).isEqualTo(pushToken);
    }

    @Test
    public void onInitPushTokenShouldSendEventWithExpectedEnvironment() throws Exception {
        mTracker.onPushTokenInited(randomString(), TRANSPORT);
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_EVENT_ID)).isEqualTo(eventId);
    }
    // endregion

    // region update token
    @Test
    public void onUpdatePushTokenShouldSendEventWithExpectedType() {
        mTracker.onPushTokenUpdated(randomString(100), TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_PUSH_TOKEN.getId());
    }

    @Test
    public void onUpdatePushTokenShouldSendEventWithExpectedName() {
        mTracker.onPushTokenUpdated(randomString(100), TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_PUSH_TOKEN.getCaption());
    }

    @Test
    public void onUpdatePushTokenShouldSendEventWithExpectedValue() {
        String expectedPushToken = randomString();
        mTracker.onPushTokenUpdated(expectedPushToken, TRANSPORT);
        checkBridge();
        assertThat(mEventValue).isEqualTo(expectedPushToken);
    }

    @Test
    public void onUpdatePushTokenShouldSendEventWithExpectedEnvironment() {
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
    public void onReceiveMessageShouldSendEventWithExpectedType() {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onReceiveMessageShouldSendEventWithExpectedName() {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onReceiveMessageShouldSendEventWithExpectedPushIdInValue() throws Exception {
        String expectedPushId = randomString();
        mTracker.onMessageReceived(expectedPushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void onReceiveMessageShouldSendEventWithExpectedActionTypeInValue() throws Exception {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_RECEIVE);
    }

    @Test
    public void onReceiveMessageShouldSendEventWithEventWithoutActionIdInValue() throws Exception {
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).has(JSON_ACTION_ID)).isFalse();
    }

    @Test
    public void onReceiveMessageShouldSendEventWithExpectedEnvironment() {
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
    public void onClearNotificationShouldSendEventWithExpectedType() {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onClearNotificationShouldSendEventWithExpectedName() {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onClearNotificationShouldSendEventWithExpectedPushIdInValue() throws Exception {
        String pushId = randomString();
        mTracker.onNotificationCleared(pushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(pushId);
    }

    @Test
    public void onClearNotificationShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_CLEAR);
    }

    @Test
    public void onClearNotificationShouldSendEventWithoutActionId() throws Exception {
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).has(JSON_ACTION_ID)).isFalse();
    }

    @Test
    public void onClearNotificationShouldSendEventWithExpectedEnvironment() {
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
    public void onOpenNotificationShouldSendEventWithExpectedTypeId() {
        mTracker.onPushOpened(randomString(), null, TRANSPORT, null);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onOpenPushNotificationShouldSendEventWithExpectedName() {
        mTracker.onPushOpened(randomString(), null, TRANSPORT, null);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onOpenPushNotificationShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onPushOpened(expectedPushId, null, TRANSPORT, null);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void onOpenPushNotificationShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onPushOpened(randomString(), null, TRANSPORT, null);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_OPEN);
    }

    @Test
    public void onOpenPushNotificationShouldSendEventWithExpectedUri() throws Exception {
        String uri = randomString();
        mTracker.onPushOpened(randomString(), null, TRANSPORT, uri);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_URI)).isEqualTo(uri);
    }

    @Test
    public void onOpenPushNotificationShouldSendEventWithoutActionId() throws Exception {
        mTracker.onPushOpened(randomString(), null, TRANSPORT, null);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).has(JSON_ACTION_ID)).isFalse();
    }

    @Test
    public void onOpenPushNotificationShouldSendEventWithExpectedEnvironment() throws Exception {
        mTracker.onPushOpened(randomString(), null, TRANSPORT, null);
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
    public void onNotificationAdditionalActionShouldSendEventWithExpectedType() {
        mTracker.onNotificationAdditionalAction(
            randomString(),
            randomString(),
            null,
            TRANSPORT,
            null
        );
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onNotificationAdditionalActionShouldSendEventWithExpectedName() {
        mTracker.onNotificationAdditionalAction(
            randomString(),
            randomString(),
            null,
            TRANSPORT,
            null
        );
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onNotificationAdditionalActionShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onNotificationAdditionalAction(
            expectedPushId,
            randomString(),
            null,
            TRANSPORT,
            null
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void onNotificationAdditionalActionShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationAdditionalAction(
            randomString(),
            randomString(),
            null,
            TRANSPORT,
            null
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_CUSTOM);
    }

    @Test
    public void onNotificationAdditionalActionShouldSendEventWithExpectedUri() throws Exception {
        String uri = randomString();
        mTracker.onNotificationAdditionalAction(
            randomString(),
            randomString(),
            null,
            TRANSPORT,
            uri
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_URI)).isEqualTo(uri);
    }

    @Test
    public void onNotificationAdditionalActionShouldSendEventWithExpectedActionId() throws Exception {
        String expectedActionId = randomString();
        mTracker.onNotificationAdditionalAction(
            randomString(),
            expectedActionId,
            null,
            TRANSPORT,
            null
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_ID)).isEqualTo(expectedActionId);
    }

    @Test
    public void onNotificationAdditionalActionShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationAdditionalAction(
            randomString(),
            randomString(),
            null,
            TRANSPORT,
            null
        );
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
    public void onNotificationInlineAdditionalActionShouldSendEventWithExpectedType() {
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT,
            null
        );
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onNotificationInlineAdditionalActionShouldSendEventWithExpectedName() {
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT,
            null
        );
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onNotificationInlineAdditionalActionShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT,
            null
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_CUSTOM);
    }

    @Test
    public void onNotificationInlineAdditionalActionShouldSendEventWithExpectedUri() throws Exception {
        String uri = randomString();
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT,
            uri
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_URI)).isEqualTo(uri);
    }

    @Test
    public void onNotificationInlineAdditionalActionShouldSendEventWithExpectedEnvironment() {
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT,
            null
        );
        checkBridge();
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_VERSION_NAME))
            .isEqualTo(BuildConfig.VERSION_NAME);
        assertThat(mEnvironment.get(AppMetricaPushEvent.EVENT_ENVIRONMENT_TRANSPORT)).isEqualTo(TRANSPORT);
    }

    @Test
    public void onNotificationInlineAdditionalActionShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onNotificationInlineAdditionalAction(
            expectedPushId,
            randomString(),
            randomString(),
            randomString(),
            TRANSPORT,
            null
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void onNotificationInlineAdditionalActionShouldSendEventWithExpectedActionId() throws Exception {
        String expectedActionId = randomString();
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            expectedActionId,
            randomString(),
            randomString(),
            TRANSPORT,
            null
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_ID)).isEqualTo(expectedActionId);
    }

    @Test
    public void onNotificationInlineAdditionalActionShouldSendEventWithExpectedText() throws Exception {
        String expectedText = randomString();
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            randomString(),
            expectedText,
            TRANSPORT,
            null
        );
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TEXT)).isEqualTo(expectedText);
    }
    // endregion

    // region process silent
    @Test
    public void onProcessSilentPushShouldSendEventWithExpectedTypeId() {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onProcessSilentPushShouldSendEventWithExpectedName() {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onProcessSilentPushShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onSilentPushProcessed(expectedPushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void onProcessSilentPushShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_PROCESSED);
    }

    @Test
    public void onProcessSilentPushShouldSendEventWithoutActionId() throws Exception {
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).has(JSON_ACTION_ID)).isFalse();
    }

    @Test
    public void onProcessSilentPushShouldSendEventWithExpectedEnvironment() {
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
    public void onNotificationShownShouldSendEventWithExpectedTypeId() {
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onNotificationShownShouldSendEventWithExpectedName() {
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onNotificationShownShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onNotificationShown(expectedPushId, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void onNotificationShownShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_SHOWN);
    }

    @Test
    public void onNotificationShownShouldSendEventWithExpectedEnvironment() {
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
    public void onNotificationIgnoredShouldSendEventWithExpectedType() {
        mTracker.onNotificationIgnored(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onNotificationIgnoredShouldSendEventWithExpectedName() {
        mTracker.onNotificationIgnored(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onNotificationIgnoredShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onNotificationIgnored(expectedPushId, randomString(),
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void onNotificationIgnoredShouldSendEventWithExpectedCategory() throws Exception {
        String expectedCategory = randomString();
        mTracker.onNotificationIgnored(randomString(), expectedCategory,
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_CATEGORY)).isEqualTo(expectedCategory);
    }

    @Test
    public void onNotificationIgnoredShouldSendEventWithExpectedDetails() throws Exception {
        String expectedDetails = randomString();
        mTracker.onNotificationIgnored(randomString(),
            randomString(), expectedDetails, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_DETAILS)).isEqualTo(expectedDetails);
    }

    @Test
    public void onNotificationIgnoredShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationIgnored(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_IGNORED);
    }

    @Test
    public void onNotificationIgnoredShouldSendEventWithExpectedEnvironment() {
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
    public void onNotificationExpiredShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        String expectedCategory = randomString();
        mTracker.onNotificationExpired(expectedPushId, expectedCategory, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_CATEGORY)).isEqualTo(expectedCategory);
    }

    @Test
    public void onNotificationDismissByTtlShouldSendEventWithExpectedActionType() throws Exception {
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
    public void onNotificationDismissByTtlShouldSendEventWithExpectedEnvironment() {
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
    public void onRemovingSilentPushProcessedShouldSendEventWithExpectedType() {
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEventType).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getId());
    }

    @Test
    public void onRemovingSilentPushProcessedShouldSendEventWithExpectedName() {
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        assertThat(mEventName).isEqualTo(AppMetricaPushEvent.EventType.EVENT_NOTIFICATION.getCaption());
    }

    @Test
    public void onRemovingSilentPushProcessedShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        mTracker.onRemovingSilentPushProcessed(expectedPushId, randomString(),
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
    }

    @Test
    public void onRemovingSilentPushProcessedShouldSendEventWithExpectedCategory() throws Exception {
        String expectedCategory = randomString();
        mTracker.onRemovingSilentPushProcessed(randomString(), expectedCategory,
            randomString(), null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_CATEGORY)).isEqualTo(expectedCategory);
    }

    @Test
    public void onRemovingSilentPushProcessedShouldSendEventWithExpectedDetails() throws Exception {
        String expectedDetails = randomString();
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), expectedDetails, null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_DETAILS)).isEqualTo(expectedDetails);
    }

    @Test
    public void onRemovingSilentPushProcessedShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onRemovingSilentPushProcessed(randomString(),
            randomString(), randomString(),
            null, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_REMOVED);
    }

    @Test
    public void onRemovingSilentPushProcessedShouldSendEventWithExpectedEnvironment() {
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
    public void onNotificationReplaceShouldSendEventWithExpectedPushId() throws Exception {
        String expectedPushId = randomString();
        String expectedNewPushId = randomString();
        mTracker.onNotificationReplace(expectedPushId, expectedNewPushId, TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getString(JSON_NOTIFICATION_ID)).isEqualTo(expectedPushId);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_NEW_PUSH_ID)).isEqualTo(expectedNewPushId);
    }

    @Test
    public void onNotificationReplaceShouldSendEventWithExpectedActionType() throws Exception {
        mTracker.onNotificationReplace(randomString(), randomString(), TRANSPORT);
        checkBridge();
        JSONObject jsonObject = new JSONObject(mEventValue);
        assertThat(jsonObject.getJSONObject(JSON_ACTION).getString(JSON_ACTION_TYPE)).isEqualTo(ACTION_TYPE_REPLACE);
    }

    @Test
    public void onNotificationReplaceShouldSendEventWithExpectedEnvironment() {
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
    public void onInitPushTokenShouldWorkCorrectlyIfAppMetricaHasNotActivatedYet() throws Exception {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onPushTokenInited(randomString(), TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onUpdatePushTokenShouldWorkCorrectlyIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onPushTokenUpdated(randomString(), TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onReceiveMessageShouldWorkCorrectlyIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onMessageReceived(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onClearNotificationShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationCleared(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onOpenNotificationShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onPushOpened(randomString(), null, TRANSPORT, null);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onNotificationAdditionalActionShouldSendEventToBridgeOfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationAdditionalAction(
            randomString(),
            randomString(),
            null,
            TRANSPORT,
            null
        );
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onNotificationInlineAdditionalActionShouldSendEventToBridgeOfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationInlineAdditionalAction(
            randomString(),
            randomString(),
            null,
            randomString(),
            TRANSPORT,
            null
        );
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onProcessSilentPushShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onSilentPushProcessed(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onNotificationShownShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationShown(randomString(), null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onNotificationIgnoredShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
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
    public void onNotificationTtlShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
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
    public void onRemovingSilentPushProcessedShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onRemovingSilentPushProcessed(randomString(), null, null, null, TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void onNotificationReplaceShouldNotSendEventToBridgeIfAppMetricaHasNotActivatedYet() {
        doThrow(new IllegalStateException(randomString())).when(AppMetrica.class);
        mTracker.onNotificationReplace(randomString(), randomString(), TRANSPORT);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
    }
}
