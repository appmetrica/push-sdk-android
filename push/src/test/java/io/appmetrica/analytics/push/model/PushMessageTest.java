package io.appmetrica.analytics.push.model;

import android.content.Context;
import android.os.Bundle;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Random;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class PushMessageTest {

    private Context mContext;
    private Bundle mEmptyBundle;
    private PushMessage mEmptyPushMessage;

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application.getApplicationContext();
        mEmptyBundle = new Bundle();
        mEmptyPushMessage = createPushMessage(mEmptyBundle);
    }

    //region ownPush
    @Test
    public void testPushMessageIsNotOwnIfPushMessageRootIsNotExists() {
        assertThat(mEmptyPushMessage.isOwnPush()).isFalse();
    }

    @Test
    public void testPushMessageIsOwnOfPushMessageRootIsExists() {
        Bundle bundle = new Bundle();
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, new JSONObject().toString());
        assertThat(createPushMessage(bundle).isOwnPush()).isTrue();
    }
    //endregion

    //region notificationId
    @Test
    public void testNotificationIdShouldBeNullIfNotDefinedRootElement() {
        assertThat(mEmptyPushMessage.getNotificationId()).isNull();
    }

    @Test
    public void testNotificationIdShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(createPushMessageWithValues("test key", "test value").getNotificationId()).isNull();
    }

    @Test
    public void testNotificationIdShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.NOTIFICATION_ID, null).getNotificationId())
            .isNull();
    }

    @Test
    public void testNotificationIdShouldBeExpectedIfDefinedInJson() throws Exception {
        String expected = new RandomStringGenerator(new Random().nextInt(100) + 1).nextString();
        assertThat(createPushMessageWithValues(Constants.PushMessage.NOTIFICATION_ID, expected).getNotificationId())
            .isEqualTo(expected);
    }
    //endregion

    //region silent
    @Test
    public void testSilentShouldBeFalseIfRootElementNotExistsInBundle() {
        assertThat(mEmptyPushMessage.isSilent()).isFalse();
    }

    @Test
    public void testSilentShouldBeFalseIfNotExistsInBundle() throws Exception {
        assertThat(createPushMessageWithValues("test key", "test value").isSilent()).isFalse();
    }

    @Test
    public void testSilentShouldBeFalseIfEmptyStringDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.SILENT, "").isSilent()).isFalse();
    }

    @Test
    public void testSilentPushShouldBeFalseIsInvalidStringDefinedInBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PushMessage.SILENT, "test string");
        assertThat(createPushMessage(bundle).isSilent()).isFalse();
    }

    @Test
    public void testSilentPushShouldBeFalseIfFalseDefinedInBundleAsString() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PushMessage.SILENT, "false");
        assertThat(createPushMessage(bundle).isSilent()).isFalse();
    }

    @Test
    public void testSilentPushShouldBeTrueIfTrueDefinedInBundleAsString() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PushMessage.SILENT, "true");
        assertThat(createPushMessageWithValues(Constants.PushMessage.SILENT, "true").isSilent()).isTrue();
    }
    //endregion

    //region payload
    @Test
    public void testPayloadShouldBeNullIfRootElementIsNotDefinedInBundle() {
        assertThat(mEmptyPushMessage.getPayload()).isNull();
    }

    @Test
    public void testPayloadShouldBeNullIfNotDefinedInRootElement() throws Exception {
        assertThat(createPushMessageWithValues("test key", "test value").getPayload()).isNull();
    }

    @Test
    public void testPayloadShouldBeNullIfNullDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.PAYLOAD, null).getPayload()).isNull();
    }

    @Test
    public void testPayloadShouldBeExpectedIfDefinedInBundle() throws Exception {
        String expected = new RandomStringGenerator(new Random().nextInt(100) + 1).nextString();
        assertThat(createPushMessageWithValues(Constants.PushMessage.PAYLOAD, expected).getPayload())
            .isEqualTo(expected);
    }
    //endregion

    //region notification
    @Test
    public void testNotificationShouldBeNullIfRootElementIsNotDefinedInBundle() {
        assertThat(mEmptyPushMessage.getNotification()).isNull();
    }

    @Test
    public void testNotificationShouldBeNullIfNotDefinedInRootElement() throws Exception {
        assertThat(createPushMessageWithValues("test key", "test value").getNotification()).isNull();
    }

    @Test
    public void testNotificationShouldBeNullIfNullDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.NOTIFICATION, null).getNotification()).isNull();
    }

    @Test
    public void testNotificationShouldBeNullIfInvalidJsonDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.NOTIFICATION, "Test").getNotification()).isNull();
    }

    @Test
    public void testNotificationShouldNotBeNullIfValidJsonDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.NOTIFICATION, new JSONObject()).getNotification())
            .isNotNull();
    }
    //endregion

    //region filters
    @Test
    public void testFiltersShouldBeNullIfRootElementIsNotDefinedInBundle() {
        assertThat(mEmptyPushMessage.getFilters()).isNull();
    }

    @Test
    public void testFiltersShouldBeNullIfNotDefinedInRootElement() throws Exception {
        assertThat(createPushMessageWithValues(randomString(), randomString()).getFilters()).isNull();
    }

    @Test
    public void testFiltersShouldBeNullIfNullDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.FILTERS, null).getFilters()).isNull();
    }

    @Test
    public void testFiltersShouldBeNullIfInvalidJsonDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.FILTERS, randomString()).getFilters()).isNull();
    }

    @Test
    public void testFiltersShouldNotBeNullIfValidJsonDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.FILTERS, new JSONObject()).getFilters())
            .isNotNull();
    }
    //endregion

    //region bundle
    @Test(expected = NullPointerException.class)
    public void testBundleShouldBeNullIfBundleInConstructorIsNull() {
        PushMessage pushMessage = createPushMessage(null);
    }

    @Test
    public void testBundleShouldBeEqualsToBundleInConstructor() {
        Bundle expected = mock(Bundle.class);
        assertThat(createPushMessage(expected).getBundle()).isEqualTo(expected);
    }
    //endregion

    //region pushIdToRemove
    @Test
    public void testPushIdToRemoveShouldBeNullIfNotDefinedRootElement() {
        assertThat(mEmptyPushMessage.getPushIdToRemove()).isNull();
    }

    @Test
    public void testPushIdToRemoveShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(createPushMessageWithValues("test key", "test value").getPushIdToRemove()).isNull();
    }

    @Test
    public void testPushIdToRemoveShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.PUSH_ID_TO_REMOVE, null).getPushIdToRemove())
            .isNull();
    }

    @Test
    public void testPushIdToRemoveShouldBeExpectedIfDefinedInJson() throws Exception {
        String expected = new RandomStringGenerator(new Random().nextInt(100) + 1).nextString();
        assertThat(createPushMessageWithValues(Constants.PushMessage.PUSH_ID_TO_REMOVE, expected).getPushIdToRemove())
            .isEqualTo(expected);
    }
    //endregion

    //region lazyPushRequestInfo
    @Test
    public void testLazyPushRequestInfoShouldBeNullIfRootElementIsNotDefinedInBundle() {
        assertThat(mEmptyPushMessage.getLazyPushRequestInfo()).isNull();
    }

    @Test
    public void testLazyPushRequestInfoShouldBeNullIfNotDefinedInRootElement() throws Exception {
        assertThat(createPushMessageWithValues(randomString(), randomString()).getLazyPushRequestInfo()).isNull();
    }

    @Test
    public void testLazyPushRequestInfoShouldBeNullIfNullDefinedInBundle() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.LAZY_PUSH, null).getLazyPushRequestInfo())
            .isNull();
    }

    @Test
    public void testLazyPushRequestInfoShouldBeNullIfInvalidJsonDefinedInBundle() throws Exception {
        assertThat(
            createPushMessageWithValues(Constants.PushMessage.LAZY_PUSH, randomString()).getLazyPushRequestInfo()
        ).isNull();
    }

    @Test
    public void testLazyPushRequestInfoShouldNotBeNullIfValidJsonDefinedInBundle() throws Exception {
        assertThat(
            createPushMessageWithValues(Constants.PushMessage.LAZY_PUSH, new JSONObject()).getLazyPushRequestInfo()
        ).isNotNull();
    }
    //endregion

    //region timeToShowMillis
    @Test
    public void testTimeToShowMillisShouldBeNullIfNotDefinedRootElement() {
        assertThat(mEmptyPushMessage.getTimeToShowMillis()).isNull();
    }

    @Test
    public void testTimeToShowMillisShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(createPushMessageWithValues("test key", "test value").getTimeToShowMillis()).isNull();
    }

    @Test
    public void testTimeToShowMillisShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createPushMessageWithValues(Constants.PushMessage.TIME_TO_SHOW_MILLIS, null).getTimeToShowMillis())
            .isNull();
    }

    @Test
    public void testTimeToShowMillisShouldBeExpectedIfDefinedInJson() throws Exception {
        Long expected = new Random().nextLong();
        assertThat(
            createPushMessageWithValues(Constants.PushMessage.TIME_TO_SHOW_MILLIS, expected).getTimeToShowMillis()
        ).isEqualTo(expected);
    }
    //endregion

    private PushMessage createPushMessageWithValues(String key, Object value) throws Exception {
        JSONObject yampJson = new JSONObject();
        yampJson.put(key, value);
        Bundle bundle = new Bundle();
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, yampJson.toString());
        return createPushMessage(bundle);
    }

    private PushMessage createPushMessage(Bundle bundle) {
        return new PushMessage(mContext, bundle);
    }
}
