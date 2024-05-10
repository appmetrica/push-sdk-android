package io.appmetrica.analytics.push.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import io.appmetrica.analytics.push.JsonUtils;
import io.appmetrica.analytics.push.impl.Constants;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowBitmap;

import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PushNotificationTests extends PushNotificationTestsData {

    public static class SimpleParametrizedTests extends PushNotificationTestsData {
        protected Object mInputValue;
        protected Object mExpectedValue;

        public SimpleParametrizedTests(Object inputValue, Object expectedValue) {
            mInputValue = inputValue;
            mExpectedValue = expectedValue;
        }
    }

    public static Collection<Object[]> generalIntFieldTestData() {

        String randomString = randomString();
        int randomPositiveInt = randomPositiveInt();
        int randomNegativeInt = randomNegativeInt();

        return Arrays.asList(new Object[][]{
            {randomString, null},
            {null, null},
            {"", null},
            {true, null},
            {0, 0},
            {randomNegativeInt, randomNegativeInt},
            {randomPositiveInt, randomPositiveInt}
        });
    }

    public static Collection<Object[]> generalLongFieldTestData() {

        String randomString = randomString();
        long randomPositiveLong = randomPositiveInt();
        long randomNegativeLong = randomNegativeInt();

        return Arrays.asList(new Object[][]{
            {randomString, null},
            {null, null},
            {"", null},
            {true, null},
            {0L, 0L},
            {randomNegativeLong, randomNegativeLong},
            {randomPositiveLong, randomPositiveLong}
        });
    }

    public static Collection<Object[]> generalStringFieldTestData() {
        String randomString = randomString();
        return Arrays.asList(new Object[][]{
            {null, ""},
            {"", ""},
            {randomString, randomString}
        });
    }

    public static Collection<Object[]> generalBooleanFieldTestData() {
        String randomString = randomString();
        int positiveInt = randomPositiveInt();
        int negativeInt = randomNegativeInt();
        return Arrays.asList(new Object[][]{
            {null, null},
            {"", null},
            {positiveInt, null},
            {negativeInt, null},
            {0, null},
            {randomString, null},
            {true, true},
            {false, false}
        });
    }

    //region mNotificationId
    @Test
    public void testNotificationIdShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getNotificationId()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class NotificationIdTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test notificationId should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalIntFieldTestData();
        }

        public NotificationIdTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testNotificationIdShouldBeExpectedForInputValue() throws Exception {
            assertThat(createPushNotification(createJsonObjectWithNotificationId(mInputValue)).getNotificationId())
                .isEqualTo((Integer) mExpectedValue);
        }

        private JSONObject createJsonObjectWithNotificationId(Object value) throws Exception {
            return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.ID, value);
        }
    }
    //endregion

    //region notificationTag
    @Test
    public void testNotificationTagShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getNotificationTag()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class NotificationTagTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test notificationTag should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public NotificationTagTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testNotificationIdShouldBeExpectedForInputValue() throws Exception {
            assertThat(createPushNotification(createJsonObjectWithNotificationTag(mInputValue)).getNotificationTag())
                .isEqualTo((String) mExpectedValue);
        }

        private JSONObject createJsonObjectWithNotificationTag(Object value) throws Exception {
            return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.TAG, value);
        }
    }
    //endregion

    //region mCategory
    @Test
    public void testCategoryShouldBeEmptyIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getCategory()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class CategoryTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test category should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public CategoryTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testNotificationCategoryShouldBeExpectedForJsonInputCategory() throws Exception {
            assertThat(createPushNotification(createJsonObjectWithCategory(mInputValue)).getCategory())
                .isEqualTo((String) mExpectedValue);
        }

        private JSONObject createJsonObjectWithCategory(Object value) throws Exception {
            return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.CATEGORY, value);
        }
    }
    //endregion

    //region mCancelable
    @Test
    public void testCancelableShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getAutoCancel()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class CancelableTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test cancelable should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public CancelableTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testNotificationCancelableShouldBeExpectedForJsonInputCancelable() throws Exception {
            assertThat(createPushNotification(createJsonObjectWithCancelable(mInputValue)).getAutoCancel())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private JSONObject createJsonObjectWithCancelable(Object value) throws Exception {
            return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AUTO_CANCEL, value);
        }
    }

    //endregion

    //region mColor
    @Test
    public void testColorShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getColor()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ColorTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test color should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalIntFieldTestData();
        }

        public ColorTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testNotificationColorShouldBeExpectedForJsonInputValue() throws Exception {
            assertThat(createPushNotificationFromJsonWithColor(mInputValue).getColor())
                .isEqualTo((Integer) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithColor(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.COLOR, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mContentTitle
    @Test
    public void testContentTitleShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getContentTitle()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ContentTitleTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test content title should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public ContentTitleTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testNotificationColorShouldBeExpectedForJsonInputValue() throws Exception {
            assertThat(createPushNotificationFromJsonWithContentTitle(mInputValue).getContentTitle())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithContentTitle(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.CONTENT_TITLE, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mContentInfo
    @Test
    public void testContentInfoShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getContentInfo()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ContentInfoTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test content info should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public ContentInfoTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testContentInfoShouldBeExpectedForInputJsonValue() throws Exception {
            assertThat(createPushNotificationFromJsonWithContentInfo(mInputValue).getContentInfo())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithContentInfo(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.CONTENT_INFO, value);
            return createPushNotification(jsonObject);
        }

    }
    //endregion

    //region mContentText
    @Test
    public void testContentTextShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getContentText()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ContentTextTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test content title should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public ContentTextTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testContentTextShouldBeExpectedForJsonInputValue() throws Exception {
            assertThat(createPushNotificationFromJsonContentText(mInputValue).getContentText())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonContentText(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.CONTENT_TEXT, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mContentSubtext
    @Test
    public void testContentSubtextShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getContentSubtext()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ContentSubtextTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test content subtext should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public ContentSubtextTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testContentSubtextShouldBeExpectedForJsonInputValue() throws Exception {
            assertThat(createPushNotificationFromJsonWithContentSubtext(mInputValue).getContentSubtext())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithContentSubtext(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.CONTENT_SUBTEXT, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mTicker
    @Test
    public void testTickerShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getTicker()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class TicketTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test ticker should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public TicketTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testTickerShouldBeExpectedForJsonInputValue() throws Exception {
            assertThat(createPushNotificationFromJsonWithTicker(mInputValue).getTicker())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithTicker(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.TICKER, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mDefaults
    @Test
    public void testDefaultsShouldBeNullIfNotExistsInJson() {
        assertThat(mEmptyPushNotification.getDefaults()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class DefaultTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test defaults should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalIntFieldTestData();
        }

        public DefaultTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testDefaultsShouldBeExpectedForJsonInputValue() throws Exception {
            assertThat(createPushNotificationFromJsonWithDefaults(mInputValue).getDefaults())
                .isEqualTo((Integer) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithDefaults(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.DEFAULTS, value);
            return createPushNotification(jsonObject);
        }

    }
    //endregion

    //region mGroup
    @Test
    public void testGroupShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getGroup()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class GroupTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test group should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public GroupTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testGroupShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithGroup(mInputValue).getGroup())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithGroup(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.GROUP, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mGroupSummary
    @Test
    public void testGroupSummaryShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getGroupSummary()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class GroupSummaryTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test group summary should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public GroupSummaryTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testGroupSummaryShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithGroupSummary(mInputValue).getGroupSummary())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithGroupSummary(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.GROUP_SUMMARY, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mLedLights
    @Test
    public void testLedLightsShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getLedLights()).isNull();
    }

    @Test
    public void testLedLightsShouldNotBeNullIfJsonObjectDefinedInJson() throws Exception {
        assertThat(createPushNotification(JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.LED_LIGHTS,
            new JSONObject())).getLedLights()).isNotNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class LedLightTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test led lights should be {1} for json input value {0}")
        public static Collection<Object[]> data() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.PushMessage.Notification.LedLights.LED_LIGHTS_COLOR, 1);
            return Arrays.asList(new Object[][]{
                {null, null},
                {"", null},
                {randomString(), null},
                {randomNegativeInt(), null},
                {randomPositiveInt(), null},
                {0, null},
                {new JSONArray(), null}
            });
        }

        public LedLightTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testLedLightsShouldBeExpectedForInputJsonValues() throws Exception {
            if (mExpectedValue == null) {
                assertThat(createPushNotificationFromJsonWithLedLights(mInputValue).getLedLights()).isNull();
            } else {
                assertThat(createPushNotificationFromJsonWithLedLights(mInputValue).getLedLights()).isNotNull();
            }
        }

        private PushNotification createPushNotificationFromJsonWithLedLights(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.LED_LIGHTS, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mDisplayedNumber
    @Test
    public void testDisplayedNumberShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getDisplayedNumber()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class DisplayedNumberTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test displayed number should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalIntFieldTestData();
        }

        public DisplayedNumberTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testDisplayedNumberShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithDisplayedNumber(mInputValue).getDisplayedNumber())
                .isEqualTo((Integer) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithDisplayedNumber(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.DISPLAYED_NUMBER, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mOngoing
    @Test
    public void testOngoingShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getOngoing()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class OngoingTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test ongoing should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public OngoingTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testOngoingShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithOngoing(mInputValue).getOngoing())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithOngoing(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.ONGOING, value);
            return createPushNotification(jsonObject);
        }
    }

    //endregion

    //region mOnlyAlertOnce
    @Test
    public void testOnlyAlertOnceShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getOnlyAlertOnce()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class OnlyAlertOnceTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test only alert once should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public OnlyAlertOnceTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testOnlyAlertOnceShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithOnlyAlertOnce(mInputValue).getOnlyAlertOnce())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithOnlyAlertOnce(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.ONLY_ALERT_ONCE, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mPriority
    @Test
    public void testPriorityShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getPriority()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class PriorityTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test priority should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalIntFieldTestData();
        }

        public PriorityTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testPriorityShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithPriority(mInputValue).getPriority())
                .isEqualTo((Integer) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithPriority(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.PRIORITY, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mWhen
    @Test
    public void testWhenShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(Math.abs(System.currentTimeMillis() - mEmptyPushNotification.getWhen()))
            .isLessThanOrEqualTo(1000);
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class WhenTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test when should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            long positiveLong = Math.abs(new Random().nextLong()) + 1;
            long negativeLong = -Math.abs(new Random().nextLong()) - 1;
            return Arrays.asList(new Object[][]{
                {"", null},
                {null, null},
                {randomString(), null},
                {true, null},
                {false, null},
                {0L, 0L},
                {positiveLong, positiveLong},
                {negativeLong, negativeLong},
            });
        }

        public WhenTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testWhenShouldBeExpectedForJsonInputValues() throws Exception {
            PushNotification notification = createPushNotificationFromJsonWithWhen(mInputValue);
            if (mExpectedValue == null) {
                assertThat(System.currentTimeMillis() - notification.getWhen()).isLessThan(1000);
            } else {
                assertThat(notification.getWhen()).isEqualTo((Long) mExpectedValue);
            }
        }

        private PushNotification createPushNotificationFromJsonWithWhen(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.WHEN, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mShowWhen
    @Test
    public void testShowWhenShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getShowWhen()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ShowWhenTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test showWhen should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public ShowWhenTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testShowWhenShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithShowWhen(mInputValue).getShowWhen())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithShowWhen(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.SHOW_WHEN, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mSortKey
    @Test
    public void testSortKeyShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getSortKey()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class SortKeyTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test sort key should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public SortKeyTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testSortKeyShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithSortKey(mInputValue).getSortKey())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithSortKey(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.SORT_KEY, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mVibrate
    @Test
    public void testVibrateShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getVibrate()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class VibrateTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test vibrate should be {1} for json input value {0}")
        public static Collection<Object[]> data() throws Exception {
            return Arrays.asList(new Object[][]{
                {null, null},
                {"", null},
                {randomString(), null},
                {randomNegativeInt(), null},
                {randomPositiveInt(), null},
                {new Random().nextBoolean(), null},
                {new Random().nextLong(), null},
            });
        }

        public VibrateTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testVibrateShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithVibrate(mInputValue).getVibrate())
                .isEqualTo((long[]) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithVibrate(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.VIBRATE, value);
            return createPushNotification(jsonObject);
        }
    }

    @Test
    public void testVibrateShouldHaveSameSizeAsJsonArray() throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < new Random().nextInt(20) + 1; i++) {
            jsonArray.put(new Random().nextLong());
        }
        assertThat(createPushNotificationFromJsonWithVibrate(jsonArray).getVibrate().length)
            .isEqualTo(jsonArray.length());
    }

    private PushNotification createPushNotificationFromJsonWithVibrate(Object value) throws Exception {
        JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.VIBRATE, value);
        return createPushNotification(jsonObject);
    }
    //endregion

    //region mVisibility
    @Test
    public void testVisibilityShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getVisibility()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class VisibilityTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test group should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalIntFieldTestData();
        }

        public VisibilityTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testVisibilityShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithVisibility(mInputValue).getVisibility())
                .isEqualTo((Integer) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithVisibility(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.VISIBILITY, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mOpenActionUrl
    @Test
    public void testOpenActionUrlShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getOpenActionUrl()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class OpenActionUrlTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test open action url should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public OpenActionUrlTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testOpenActionUrlShoudBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithOpenActionUrl(mInputValue).getOpenActionUrl())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithOpenActionUrl(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.OPEN_ACTION, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mAdditionalActions
    @Test
    public void testAdditionalActionsShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getAdditionalActions()).isNull();
    }

    @Test
    public void testAdditionalActionsShouldBeNullIfNullDefinedInJson() throws Exception {
        testAdditionalActionsShouldBeNull(null);
    }

    @Test
    public void testAdditionalActionsShouldBeNullIfEmptyStringDefinedInJson() throws Exception {
        testAdditionalActionsShouldBeNull("");
    }

    @Test
    public void testAdditionalActionsShouldBeNullIfStringDefinedInJson() throws Exception {
        testAdditionalActionsShouldBeNull(randomString());
    }

    @Test
    public void testAdditionalActionsShouldBeNullIfIntegerDefinedInJson() throws Exception {
        testAdditionalActionsShouldBeNull(new Random().nextInt());
    }

    @Test
    public void testAdditionalActionsShouldBeNullIfBooleanDefinedInJson() throws Exception {
        testAdditionalActionsShouldBeNull(new Random().nextBoolean());
    }

    @Test
    public void testAdditionalActionsShouldBeNullIfJsonObjectDefinedInJson() throws Exception {
        testAdditionalActionsShouldBeNull(new JSONObject());
    }

    @Test
    public void testAdditionalActionsShouldBeNullIfSomeElementsAreIncorrectInJsonArray() throws Exception {
        testAdditionalActionsShouldBeNull(new JSONArray().put(new JSONObject()).put(1).put(new JSONObject()));
    }

    @Test
    public void testAdditionalActionsShouldHaveSameSizeAsInJson() throws Exception {
        JSONArray expected = new JSONArray();
        for (int i = 0; i < new Random().nextInt(20) + 1; i++) {
            expected.put(new JSONObject());
        }
        assertThat(createPushNotificationFromJsonWithAdditionalActions(expected).getAdditionalActions().length)
            .isEqualTo(expected.length());
    }

    private void testAdditionalActionsShouldBeNull(Object value) throws Exception {
        assertThat(createPushNotificationFromJsonWithAdditionalActions(value).getAdditionalActions()).isNull();
    }

    private PushNotification createPushNotificationFromJsonWithAdditionalActions(Object value) throws Exception {
        JSONObject jsonObject =
            JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.ADDITIONAL_ACTIONS, value);
        return createPushNotification(jsonObject);
    }
    //endregion

    //region mIconResId
    @Test
    public void testIconResIdShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getIconResId()).isNull();
    }

    @Test
    public void testIconResIdShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithIconResId(null).getIconResId()).isNull();
    }

    @Test
    public void testIconResIdShouldBeNullIfEmptyStringDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithIconResId("").getIconResId()).isNull();
    }

    @Test
    public void testIconResIdShouldBeNullIfZeroDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithIconResId(0).getIconResId()).isNull();
    }

    @Test
    @SuppressLint("NewApi")
    public void testIconResIdShouldBeExpectedNumberIfNumberDefinedInJson() throws Exception {
        int expected = new Random().nextInt(Integer.MAX_VALUE - 1) + 1;
        when(mResources.getDrawable(expected, null)).thenReturn(mock(Drawable.class));
        assertThat(createPushNotificationFromJsonWithIconResId(expected).getIconResId()).isEqualTo(expected);
    }

    @Test
    public void testIconResIdShouldBeValidResIdIfDrawableWithDefinedResNameExists() throws Exception {
        String resName = randomString();
        Integer expectedResId = new Random().nextInt();
        when(mResources.getIdentifier(resName, "drawable", mContext.getPackageName()))
            .thenReturn(expectedResId);
        assertThat(createPushNotificationFromJsonWithIconResId(resName).getIconResId()).isEqualTo(expectedResId);
    }

    @Test
    public void testIconResIdShouldBeNullIfDrawableWithDefinedResNameIsNotExists() throws Exception {
        assertThat(createPushNotificationFromJsonWithIconResId(randomString()).getIconResId()).isNull();
    }

    private PushNotification createPushNotificationFromJsonWithIconResId(Object value) throws Exception {
        JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.ICON_RES_ID, value);
        return createPushNotification(jsonObject);
    }
    //endregion

    //region largeIconResId
    @Test
    public void testLargeIconResIdShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getLargeIconResId()).isNull();
    }

    @Test
    public void testLargeIconResIdShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithLargeIconResId(null).getLargeIconResId()).isNull();
    }

    @Test
    public void testLargeIconResIdShouldBeNullIfEmptyStringDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithLargeIconResId("").getLargeIconResId()).isNull();
    }

    @Test
    public void testLargeIconResIdShouldBeNullIfZeroDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithLargeIconResId(0).getLargeIconResId()).isNull();
    }

    @Test
    @SuppressLint("NewApi")
    public void testLargeIconResIdShouldBeExpectedNumberIfNumberDefinedInJson() throws Exception {
        int expected = new Random().nextInt(Integer.MAX_VALUE - 1) + 1;
        when(mResources.getDrawable(expected, null)).thenReturn(mock(Drawable.class));
        assertThat(createPushNotificationFromJsonWithLargeIconResId(expected).getLargeIconResId()).isEqualTo(expected);
    }

    @Test
    public void testLargeIconResIdShouldBeValidResIdIfDrawableWithDefinedResNameExists() throws Exception {
        String resName = randomString();
        Integer expectedResId = new Random().nextInt();
        when(mResources.getIdentifier(resName, "drawable", mContext.getPackageName()))
            .thenReturn(expectedResId);
        assertThat(createPushNotificationFromJsonWithLargeIconResId(resName).getLargeIconResId())
            .isEqualTo(expectedResId);
    }

    @Test
    public void testLargeIconResIdShouldBeNullIfDrawableWithDefinedResNameIsNotExists() throws Exception {
        assertThat(createPushNotificationFromJsonWithLargeIconResId(randomString()).getLargeIconResId()).isNull();
    }

    private PushNotification createPushNotificationFromJsonWithLargeIconResId(Object value) throws Exception {
        JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.LARGE_ICON_ID, value);
        return createPushNotification(jsonObject);
    }
    //endregion

    //region largeIconUrl
    @Test
    public void testLargeIconUrlShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getLargeIconUrl()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class LargeIconUrlTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test large icon url should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public LargeIconUrlTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testLargeIconUrlShoudBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithLargeIconUrl(mInputValue).getLargeIconUrl())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithLargeIconUrl(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.LARGE_ICON_URL, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region largeIcon
    @Config(shadows = ShadowBitmap.class)
    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class LargeIconTests extends SimpleParametrizedTests {

        private static final String FROM_RESOURCES = "get from resources";
        private static final String DOWNLOAD = "download";

        private final Integer resId;
        private final boolean resourceExist;
        private final String url;
        private final boolean urlResourceExist;

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test large icon should {4} for res id {0}(exist: {1}) and url {2}(exist: {3})"
        )
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                {null, false, null, false, null},
                {null, false, randomString(), true, DOWNLOAD},
                {null, false, randomString(), false, null},
                {randomInt(), true, null, false, FROM_RESOURCES},
                {randomInt(), true, randomString(), true, FROM_RESOURCES},
                {randomInt(), true, randomString(), false, FROM_RESOURCES},
                {randomInt(), false, null, false, null},
                {randomInt(), false, randomString(), true, DOWNLOAD},
                {randomInt(), false, randomString(), false, null}
            });
        }

        public LargeIconTests(
            Object resId,
            Object resourceExist,
            Object url,
            Object urlResourceExist,
            Object expectedValue
        ) {
            super(null, expectedValue);
            this.resId = (Integer) resId;
            this.resourceExist = (Boolean) resourceExist;
            this.url = (String) url;
            this.urlResourceExist = (Boolean) urlResourceExist;
        }

        @Test
        public void testLargeIconShouldBeExpectedForJsonInputValues() throws Exception {
            Drawable drawable = mock(Drawable.class);
            when(drawable.getBounds()).thenReturn(new Rect(0, 0, 10, 10));
            Bitmap bitmap = mock(Bitmap.class);
            when(mResources.getDisplayMetrics()).thenReturn(new DisplayMetrics());
            when(mResources.getDrawable(resId == null ? anyInt() : eq(resId), nullable(Resources.Theme.class)))
                .thenReturn(resourceExist ? drawable : null);
            when(mBitmapLoader.get(eq(mContext), eq(url), any(Float.class), any(Float.class)))
                .thenReturn(urlResourceExist ? bitmap : null);
            Bitmap largeIcon = createPushNotificationFromJsonWithLargeIconIdAndLargeIconUrl(resId, url).getLargeIcon();
            if (mExpectedValue == FROM_RESOURCES) {
                verify(mBitmapLoader, never()).get(any(Context.class), anyString(), anyFloat(), anyFloat());
            } else if (mExpectedValue == DOWNLOAD) {
                assertThat(largeIcon).isEqualTo(bitmap);
            } else {
                assertThat(largeIcon).isNull();
            }
        }

        private PushNotification createPushNotificationFromJsonWithLargeIconIdAndLargeIconUrl(
            Integer resId,
            String url
        ) throws Exception {
            JSONObject jsonObject = new JSONObject()
                .put(Constants.PushMessage.Notification.LARGE_ICON_ID, resId)
                .put(Constants.PushMessage.Notification.LARGE_ICON_URL, url);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region largeBitmapResId
    @Test
    public void testLargeBitmapResIdShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getLargeBitmapResId()).isNull();
    }

    @Test
    public void testLargeBitmapResIdShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithLargeBitmapResId(null).getLargeBitmapResId()).isNull();
    }

    @Test
    public void testLargeBitmapResIdShouldBeNullIfEmptyStringDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithLargeBitmapResId("").getLargeBitmapResId()).isNull();
    }

    @Test
    public void testLargeBitmapResIdShouldBeNullIfZeroDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithLargeBitmapResId(0).getLargeBitmapResId()).isNull();
    }

    @Test
    @SuppressLint("NewApi")
    public void testLargeBitmapResIdShouldBeExpectedNumberIfNumberDefinedInJson() throws Exception {
        int expected = new Random().nextInt(Integer.MAX_VALUE - 1) + 1;
        when(mResources.getDrawable(expected, null)).thenReturn(mock(Drawable.class));
        assertThat(createPushNotificationFromJsonWithLargeBitmapResId(expected).getLargeBitmapResId())
            .isEqualTo(expected);
    }

    @Test
    public void testLargeBitmapResIdShouldBeValidResIdIfDrawableWithDefinedResNameExists() throws Exception {
        String resName = randomString();
        Integer expectedResId = new Random().nextInt();
        when(mResources.getIdentifier(resName, "drawable", mContext.getPackageName()))
            .thenReturn(expectedResId);
        assertThat(createPushNotificationFromJsonWithLargeBitmapResId(resName).getLargeBitmapResId())
            .isEqualTo(expectedResId);
    }

    @Test
    public void testLargeBitmapResIdShouldBeNullIfDrawableWithDefinedResNameIsNotExists() throws Exception {
        assertThat(createPushNotificationFromJsonWithLargeBitmapResId(randomString()).getLargeBitmapResId()).isNull();
    }

    private PushNotification createPushNotificationFromJsonWithLargeBitmapResId(Object value) throws Exception {
        JSONObject jsonObject =
            JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.LARGE_BITMAP_ID, value);
        return createPushNotification(jsonObject);
    }
    //endregion

    //region largeBitmap
    @Config(shadows = ShadowBitmap.class)
    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class LargeBitmapTests extends SimpleParametrizedTests {

        private static final String FROM_RESOURCES = "get from resources";
        private static final String DOWNLOAD = "download";

        private final Integer resId;
        private final boolean resourceExist;
        private final String url;
        private final boolean urlResourceExist;

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test large bitmap should {4} for res id {0}(exist: {1}) and url {2}(exist: {3})"
        )
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                {null, false, null, false, null},
                {null, false, randomString(), true, DOWNLOAD},
                {null, false, randomString(), false, null},
                {randomInt(), true, null, false, FROM_RESOURCES},
                {randomInt(), true, randomString(), true, FROM_RESOURCES},
                {randomInt(), true, randomString(), false, FROM_RESOURCES},
                {randomInt(), false, null, false, null},
                {randomInt(), false, randomString(), true, DOWNLOAD},
                {randomInt(), false, randomString(), false, null}
            });
        }

        public LargeBitmapTests(
            Object resId,
            Object resourceExist,
            Object url,
            Object urlResourceExist,
            Object expectedValue
        ) {
            super(null, expectedValue);
            this.resId = (Integer) resId;
            this.resourceExist = (Boolean) resourceExist;
            this.url = (String) url;
            this.urlResourceExist = (Boolean) urlResourceExist;
        }

        @Test
        public void testLargeIconShouldBeExpectedForJsonInputValues() throws Exception {
            Drawable drawable = mock(Drawable.class);
            when(drawable.getBounds()).thenReturn(new Rect(0, 0, 10, 10));
            Bitmap bitmap = mock(Bitmap.class);
            when(mResources.getDisplayMetrics()).thenReturn(new DisplayMetrics());
            when(mResources.getDrawable(resId == null ? anyInt() : eq(resId), nullable(Resources.Theme.class)))
                .thenReturn(resourceExist ? drawable : null);
            when(mBitmapLoader.get(eq(mContext), eq(url), any(Float.class), any(Float.class)))
                .thenReturn(urlResourceExist ? bitmap : null);
            Bitmap largeBitmap =
                createPushNotificationFromJsonWithLargeBitmapIdAndLargeBitmapUrl(resId, url).getLargeBitmap();
            if (mExpectedValue == FROM_RESOURCES) {
                verify(mBitmapLoader, never()).get(any(Context.class), anyString(), anyFloat(), anyFloat());
            } else if (mExpectedValue == DOWNLOAD) {
                assertThat(largeBitmap).isEqualTo(bitmap);
            } else {
                assertThat(largeBitmap).isNull();
            }
        }

        private PushNotification createPushNotificationFromJsonWithLargeBitmapIdAndLargeBitmapUrl(
            Integer resId,
            String url
        ) throws Exception {
            JSONObject jsonObject = new JSONObject()
                .put(Constants.PushMessage.Notification.LARGE_BITMAP_ID, resId)
                .put(Constants.PushMessage.Notification.LARGE_BITMAP_URL, url);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region mSoundEnabled
    @Test
    public void testSoundEnabledShouldBeFalseIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.isSoundEnabled()).isFalse();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class SoundEnabledTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test sound enabled should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                {null, false},
                {"", false},
                {randomNegativeInt(), false},
                {randomPositiveInt() + 1, false},
                {randomString(), false},
                {true, false},
                {false, false},
                {0, false},
                {1, true}
            });
        }

        public SoundEnabledTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testSoundEnabledShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithSoundEnabled(mInputValue).isSoundEnabled())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithSoundEnabled(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.SOUND_ENABLED, value);
            return createPushNotification(jsonObject);
        }
    }

    @Test
    public void testSoundEnabledShouldBeFalseIfNullDefinedInJson() throws Exception {
        testSoundEnabledShouldBeFalse(null);
    }

    @Test
    public void testSoundEnabledShouldBeFalseIfEmptyStringDefinedIsJson() throws Exception {
        testSoundEnabledShouldBeFalse("");
    }

    @Test
    public void testSoundEnabledShouldBeFalseIsZeroDefinedInJson() throws Exception {
        testSoundEnabledShouldBeFalse(0);
    }

    @Test
    public void testSoundEnabledShouldBeFalseIfLessThanZeroDefinedInJson() throws Exception {
        testSoundEnabledShouldBeFalse(-1);
    }

    @Test
    public void testSoundEnabledShouldBeFalseIsGreaterThanOneDefinedInJson() throws Exception {
        testSoundEnabledShouldBeFalse(2);
    }

    @Test
    public void testSoundEnabledShouldBeTrueIfExactlyOneDefinedInJson() throws Exception {
        assertThat(createPushNotificationFromJsonWithSoundEnabled(1).isSoundEnabled()).isTrue();
    }

    private void testSoundEnabledShouldBeFalse(Object value) throws Exception {
        assertThat(createPushNotificationFromJsonWithSoundEnabled(value).isSoundEnabled()).isFalse();
    }

    private PushNotification createPushNotificationFromJsonWithSoundEnabled(Object value) throws Exception {
        JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.SOUND_ENABLED, value);
        return createPushNotification(jsonObject);
    }
    //endregion

    //region mSoundId
    @Test
    public void testSoundUriShouldBeDefaultIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getSoundUri()).isNull();
    }

    @Test
    public void testSoundIdShouldBeDefaultIfNotExists() throws JSONException {
        when(mResources.openRawResource(anyInt())).thenThrow(new Resources.NotFoundException());
        final PushNotification notification = createPushNotification(new JSONObject()
            .put(Constants.PushMessage.Notification.SOUND_RES_ID, randomInt())
        );
        assertThat(notification.getSoundUri()).isNull();
    }

    @Test
    public void testSoundIdShouldBeExpectedIfExists() throws JSONException {
        final int soundId = randomInt();
        final String resourcePackageName = "ru.yandex.test";
        when(mResources.openRawResource(soundId)).thenReturn(mock(InputStream.class));
        when(mResources.getResourcePackageName(soundId)).thenReturn(resourcePackageName);
        when(mResources.getResourceTypeName(soundId)).thenReturn("raw");
        when(mResources.getResourceEntryName(soundId)).thenReturn(String.valueOf(soundId));
        final PushNotification notification = createPushNotification(new JSONObject()
            .put(Constants.PushMessage.Notification.SOUND_RES_ID, soundId)
        );
        assertThat(notification.getSoundUri()).isEqualTo(
            Uri.parse("android.resource://" + resourcePackageName + "/raw/" + soundId)
        );
    }

    @Test
    public void testSoundIdShouldBeValidResIdIfSoundWithDefinedResNameExists() throws Exception {
        final String resName = randomString();
        final String resourcePackageName = "ru.yandex.test";
        final int expectedResId = randomInt();
        when(mResources.getIdentifier(resName, "raw", mContext.getPackageName())).thenReturn(expectedResId);
        when(mResources.getResourcePackageName(expectedResId)).thenReturn(resourcePackageName);
        when(mResources.getResourceTypeName(expectedResId)).thenReturn("raw");
        when(mResources.getResourceEntryName(expectedResId)).thenReturn(String.valueOf(expectedResId));
        final PushNotification notification = createPushNotification(new JSONObject()
            .put(Constants.PushMessage.Notification.SOUND_RES_ID, resName)
        );
        assertThat(notification.getSoundUri()).isEqualTo(
            Uri.parse("android.resource://" + resourcePackageName + "/raw/" + expectedResId)
        );
    }
    //endregion

    //region mChannelId
    @Test
    public void testChannelIdShouldBeEmptyIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getChannelId()).isEmpty();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ChannelIdTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test channel id should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public ChannelIdTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testChannelIdShoudBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithChannelId(mInputValue).getChannelId())
                .isEqualTo((String) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithChannelId(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.CHANNEL_ID, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region explicitIntent
    @Test
    public void testExplicitIntentShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getExplicitIntent()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ExplicitIntentTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test explicitIntent should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public ExplicitIntentTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testShowWhenShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithExplicitIntent(mInputValue).getExplicitIntent())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithExplicitIntent(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.EXPLICIT_INTENT, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region notificationTtl
    @Test
    public void testNotificationTtlShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getNotificationTtl()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class NotificationTtlTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test notificationTtl should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalLongFieldTestData();
        }

        public NotificationTtlTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testNotificationIdShouldBeExpectedForInputValue() throws Exception {
            assertThat(createPushNotification(createJsonObjectWithNotificationId(mInputValue)).getNotificationTtl())
                .isEqualTo((Long) mExpectedValue);
        }

        private JSONObject createJsonObjectWithNotificationId(Object value) throws Exception {
            return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.NOTIFICATION_TTL, value);
        }
    }
    //endregion

    //region timeToHideMillis
    @Test
    public void testTimeToHideMillisShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getTimeToHideMillis()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class TimeToHideMillisTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test timeToHideMillis should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalLongFieldTestData();
        }

        public TimeToHideMillisTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testNotificationIdShouldBeExpectedForInputValue() throws Exception {
            assertThat(createPushNotification(createJsonObjectWithNotificationId(mInputValue)).getTimeToHideMillis())
                .isEqualTo((Long) mExpectedValue);
        }

        private JSONObject createJsonObjectWithNotificationId(Object value) throws Exception {
            return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.TIME_TO_HIDE_MILLIS, value);
        }
    }
    //endregion

    //region useFlagActivityNewTask
    @Test
    public void testUseFlagActivityNewTaskShouldBeTrueIfNotDefinedInJson() throws Exception {
        assertThat(mEmptyPushNotification.getUseFlagActivityNewTask()).isTrue();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class UseFlagActivityNewTaskTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test useFlagActivityNewTask should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            String randomString = randomString();
            int positiveInt = randomPositiveInt();
            int negativeInt = randomNegativeInt();
            return Arrays.asList(new Object[][]{
                {null, true},
                {"", true},
                {positiveInt, true},
                {negativeInt, true},
                {0, true},
                {randomString, true},
                {true, true},
                {false, false}
            });
        }

        public UseFlagActivityNewTaskTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testUseFlagActivityNewTaskShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotificationFromJsonWithExplicitIntent(mInputValue).getUseFlagActivityNewTask())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private PushNotification createPushNotificationFromJsonWithExplicitIntent(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.USE_ACTIVITY_NEW_TASK_FLAG, value);
            return createPushNotification(jsonObject);
        }
    }
    //endregion

    //region open type
    @Test
    public void testOpenTypeShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyPushNotification.getOpenType()).isEqualTo(OpenType.UNKNOWN);
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class OpenTypeTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test type should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                {null, OpenType.UNKNOWN},
                {"", OpenType.UNKNOWN},
                {0, OpenType.BROADCAST},
                {1, OpenType.TRANSPARENT_ACTIVITY},
                {2, OpenType.APPLICATION_ACTIVITY},
                {randomPositiveInt() + 4, OpenType.UNKNOWN},
                {randomString(), OpenType.UNKNOWN},
                {true, OpenType.UNKNOWN},
                {false, OpenType.UNKNOWN}
            });
        }

        public OpenTypeTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testHideQuickControlPanelShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotification(mInputValue).getOpenType())
                .isEqualTo((OpenType) mExpectedValue);
        }

        private PushNotification createPushNotification(Object value) throws Exception {
            JSONObject jsonObject = JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.OPEN_TYPE, value);
            return this.createPushNotification(jsonObject);
        }
    }
    //endregion
}
