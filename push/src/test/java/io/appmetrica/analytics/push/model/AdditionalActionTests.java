package io.appmetrica.analytics.push.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import io.appmetrica.analytics.push.JsonUtils;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.model.PushNotificationTests.generalBooleanFieldTestData;
import static io.appmetrica.analytics.push.model.PushNotificationTests.generalLongFieldTestData;
import static io.appmetrica.analytics.push.model.PushNotificationTests.generalStringFieldTestData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AdditionalActionTests {

    public static class SimpleParametrizedTests extends PushNotificationTestsData {
        protected Context mContext;
        protected Object mInputValue;
        protected Object mExpectedValue;

        @Before
        public void setUp() {
            mContext = spy(RuntimeEnvironment.application.getApplicationContext());
        }

        public SimpleParametrizedTests(Object inputValue, Object expectedValue) {
            mInputValue = inputValue;
            mExpectedValue = expectedValue;
        }
    }

    private static final String TITLE_FALLBACK = "";
    private static final String URL_FALLBACK = "";
    private static final String LABEL_FALLBACK = "";

    private Context mContext;
    private Resources mResources;

    @Before
    public void setUp() {
        mContext = spy(RuntimeEnvironment.application.getApplicationContext());
        mResources = mock(Resources.class);
        when(mContext.getResources()).thenReturn(mResources);
    }

    //region mId
    @Test
    public void testIdShouldBeEmptyIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getId()).isEmpty();
    }

    @Test
    public void testIdShouldBeEmptyIfNullDefinedInJson() throws Exception {
        testIdShouldBeEmpty(null);
    }

    @Test
    public void testIdShouldBeEmptyIfEmptyStringDefinedInJson() throws Exception {
        testIdShouldBeEmpty("");
    }

    @Test
    public void testIdShouldBeExpectedIfDefinedInJson() throws Exception {
        String expected = randomString();
        assertThat(new AdditionalAction(mContext, createJsonWithId(expected)).getId())
            .isEqualTo(expected);
    }

    private void testIdShouldBeEmpty(Object value) throws Exception {
        assertThat(new AdditionalAction(mContext, createJsonWithId(value)).getId()).isEmpty();
    }

    private JSONObject createJsonWithId(Object value) throws Exception {
        return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AdditionalAction.ID, value);
    }
    //endregion

    //region mTitle
    @Test
    public void testTitleShouldBeFallbackIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getTitle())
            .isEqualTo(TITLE_FALLBACK);
    }

    @Test
    public void testTitleShouldBeFallbackIfNullInJson() throws Exception {
        testTitleShouldBeFallback(null);
    }

    @Test
    public void testTitleShouldBeExpectedIfDefinedInJson() throws Exception {
        String expected = randomString();
        assertThat(new AdditionalAction(mContext, createJsonWithTitle(expected)).getTitle())
            .isEqualTo(expected);
    }

    private void testTitleShouldBeFallback(Object value) throws Exception {
        assertThat(new AdditionalAction(mContext, createJsonWithTitle(value)).getTitle())
            .isEqualTo(TITLE_FALLBACK);
    }

    private JSONObject createJsonWithTitle(Object value) throws Exception {
        return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AdditionalAction.TITLE, value);
    }
    //endregion

    //region mActionUrl
    @Test
    public void testUrlShouldBeFallbackIfNoDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getActionUrl())
            .isEqualTo(URL_FALLBACK);
    }

    @Test
    public void testUrlShouldBeFallbackIfNullInJson() throws Exception {
        testUrlShouldBeFallback(null);
    }

    @Test
    public void testUrlShouldBeExpectedIfDefinedInJson() throws Exception {
        String expected = randomString();
        assertThat(new AdditionalAction(mContext, createJsonWithUrl(expected)).getActionUrl())
            .isEqualTo(expected);
    }

    private void testUrlShouldBeFallback(Object value) throws Exception {
        assertThat(new AdditionalAction(mContext, createJsonWithUrl(value)).getActionUrl())
            .isEqualTo(URL_FALLBACK);
    }

    private JSONObject createJsonWithUrl(Object value) throws Exception {
        return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AdditionalAction.ACTION_URL, value);
    }
    //endregion

    //region mIconResId
    @Test
    public void testIconResIdShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getIconResId()).isNull();
    }

    @Test
    public void testIconResIdShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(new AdditionalAction(mContext, createJsonWithIconResId(null)).getIconResId())
            .isNull();
    }

    @Test
    public void testIconResIdShouldBeNullIfEmptyStringDefinedInJson() throws Exception {
        assertThat(new AdditionalAction(mContext, createJsonWithIconResId("")).getIconResId())
            .isNull();
    }

    @Test
    public void testIconResIdShouldBeNullIfZeroDefinedInJson() throws Exception {
        assertThat(new AdditionalAction(mContext, createJsonWithIconResId(0)).getIconResId())
            .isNull();
    }

    @Test
    public void testIconResIdShouldBeExpectedIfNumberDefinedInJson() throws Exception {
        int expected = new Random().nextInt(Integer.MAX_VALUE - 1) + 1;
        when(mResources.getDrawable(expected, null)).thenReturn(mock(Drawable.class));
        assertThat(new AdditionalAction(mContext, createJsonWithIconResId(expected)).getIconResId())
            .isEqualTo(expected);
    }

    @Test
    public void testIconResIdShouldBeValidResIdIfDrawableWithDefinedNameExists() throws Exception {
        String resName = randomString();
        Integer expectedResId = Math.abs(new Random().nextInt());
        when(mResources.getIdentifier(resName, "drawable", mContext.getPackageName()))
            .thenReturn(expectedResId);
        assertThat(new AdditionalAction(mContext, createJsonWithIconResId(resName)).getIconResId())
            .isEqualTo(expectedResId);
    }

    @Test
    public void testIconResIdShouldBeNullIfDrawableWithDefinedNameInNotExists() throws Exception {
        assertThat(
            new AdditionalAction(mContext, createJsonWithIconResId(randomString())).getIconResId()
        ).isNull();
    }

    private JSONObject createJsonWithIconResId(Object value) throws Exception {
        return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AdditionalAction.ICON_RES_ID, value);
    }
    //endregion

    //region mHideQuickControlPanel
    @Test
    public void testHideQuickControlPanelShouldBeNullIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getHideQuickControlPanel())
            .isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class HideQuickControlPanelTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test hide quick control panel should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public HideQuickControlPanelTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testHideQuickControlPanelShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotification(mInputValue).getHideQuickControlPanel())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private AdditionalAction createPushNotification(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(
                    Constants.PushMessage.Notification.AdditionalAction.HIDE_QUICK_CONTROL_PANEL,
                    value
                );
            return new AdditionalAction(mContext, jsonObject);
        }
    }
    //endregion

    //region mAutoCancel
    @Test
    public void testAutoCancelShouldBeNullIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getAutoCancel()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class AutoCancelTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test auto cancel should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public AutoCancelTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testAutoCancelShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotification(mInputValue).getAutoCancel())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private AdditionalAction createPushNotification(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AdditionalAction.AUTO_CANCEL, value);
            return new AdditionalAction(mContext, jsonObject);
        }
    }
    //endregion

    //region explicitIntent
    @Test
    public void testExplicitIntentShouldBeNullIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getExplicitIntent()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class ExplicitIntentTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test explicit intent should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalBooleanFieldTestData();
        }

        public ExplicitIntentTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testAutoCancelShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotification(mInputValue).getExplicitIntent())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private AdditionalAction createPushNotification(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(
                    Constants.PushMessage.Notification.AdditionalAction.EXPLICIT_INTENT,
                    value
                );
            return new AdditionalAction(mContext, jsonObject);
        }
    }
    //endregion

    //region type
    @Test
    public void testTypeShouldBeNullIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getType()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class TypeTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test type should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                {null, null},
                {"", null},
                {0, AdditionalActionType.OPEN_URI},
                {1, AdditionalActionType.OPEN_APP_URI},
                {2, AdditionalActionType.DO_NOTHING},
                {3, AdditionalActionType.INLINE},
                {randomPositiveInt() + 4, AdditionalActionType.UNKNOWN},
                {randomString(), null},
                {true, null},
                {false, null}
            });
        }

        public TypeTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testHideQuickControlPanelShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotification(mInputValue).getType())
                .isEqualTo((AdditionalActionType) mExpectedValue);
        }

        private AdditionalAction createPushNotification(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AdditionalAction.TYPE, value);
            return new AdditionalAction(mContext, jsonObject);
        }
    }
    //endregion

    //region label
    @Test
    public void testLabelShouldBeFallbackIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getLabel())
            .isEqualTo(LABEL_FALLBACK);
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class LabelTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(name = "Test label should be {1} for json input value {0}")
        public static Collection<Object[]> data() {
            return generalStringFieldTestData();
        }

        public LabelTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testLabelShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotification(mInputValue).getLabel())
                .isEqualTo((String) mExpectedValue);
        }

        private AdditionalAction createPushNotification(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AdditionalAction.LABEL, value);
            return new AdditionalAction(mContext, jsonObject);
        }
    }
    //endregion

    //region label
    @Test
    public void testHideAfterSecondShouldBeFallbackIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getHideAfterSecond()).isNull();
    }

    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class HideAfterSecondTests extends SimpleParametrizedTests {

        @ParameterizedRobolectricTestRunner.Parameters(
            name = "Test HideAfterSecond should be {1} for json input value {0}"
        )
        public static Collection<Object[]> data() {
            return generalLongFieldTestData();
        }

        public HideAfterSecondTests(Object inputValue, Object expectedValue) {
            super(inputValue, expectedValue);
        }

        @Test
        public void testHideAfterSecondShouldBeExpectedForJsonInputValues() throws Exception {
            assertThat(createPushNotification(mInputValue).getHideAfterSecond())
                .isEqualTo((Long) mExpectedValue);
        }

        private AdditionalAction createPushNotification(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(
                    Constants.PushMessage.Notification.AdditionalAction.HIDE_AFTER_SECONDS,
                    value
                );
            return new AdditionalAction(mContext, jsonObject);
        }
    }
    //endregion

    //region open type
    @Test
    public void testOpenTypeShouldBeNullIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getOpenType())
            .isEqualTo(OpenType.UNKNOWN);
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

        private AdditionalAction createPushNotification(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.AdditionalAction.OPEN_TYPE, value);
            return new AdditionalAction(mContext, jsonObject);
        }
    }
    //endregion

    //region useFlagActivityNewTask
    @Test
    public void testUseFlagActivityNewTaskShouldBeTrueIfNotDefinedInJson() {
        assertThat(new AdditionalAction(mContext, new JSONObject()).getUseFlagActivityNewTask())
            .isTrue();
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
            assertThat(createPushNotification(mInputValue).getUseFlagActivityNewTask())
                .isEqualTo((Boolean) mExpectedValue);
        }

        private AdditionalAction createPushNotification(Object value) throws Exception {
            JSONObject jsonObject =
                JsonUtils.createJsonWithObject(
                    Constants.PushMessage.Notification.AdditionalAction.USE_ACTIVITY_NEW_TASK_FLAG,
                    value
                );
            return new AdditionalAction(mContext, jsonObject);
        }
    }
    //endregion

    private String randomString() {
        return new RandomStringGenerator(new Random().nextInt(100) + 1).nextString();
    }
}
