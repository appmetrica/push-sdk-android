package io.appmetrica.analytics.push.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.Constants;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomBoolean;
import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static io.appmetrica.analytics.push.testutils.Rand.randomLong;
import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class FiltersTest {

    protected Filters mEmptyFilters;

    @Before
    public void setUp() {
        mEmptyFilters = createFilters(new JSONObject());
    }

    //region maxPushPerDay
    @Test
    public void testMaxPushPerDayShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getMaxPushPerDay()).isNull();
    }

    @Test
    public void testMaxPushPerDayShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithMaxPushPerDay(null).getMaxPushPerDay()).isNull();
    }

    @Test
    public void testMaxPushPerDayShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithMaxPushPerDay(randomString()).getMaxPushPerDay()).isNull();
    }

    @Test
    public void testMaxPushPerDayShouldBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createFiltersWithMaxPushPerDay(expected).getMaxPushPerDay()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithMaxPushPerDay(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.MAX_PUSH_PER_DAY, value);
    }
    //endregion

    //region onePushPerPeriodMinutes
    @Test
    public void testOnePushPerPeriodMinutesShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getOnePushPerPeriodMinutes()).isNull();
    }

    @Test
    public void testOnePushPerPeriodMinutesShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithOnePushPerPeriodMinutes(null).getOnePushPerPeriodMinutes()).isNull();
    }

    @Test
    public void testOnePushPerPeriodMinutesShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithOnePushPerPeriodMinutes(randomString()).getOnePushPerPeriodMinutes()).isNull();
    }

    @Test
    public void testOnePushPerPeriodMinutesShouldBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createFiltersWithOnePushPerPeriodMinutes(expected).getOnePushPerPeriodMinutes()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithOnePushPerPeriodMinutes(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.ONE_PUSH_PER_PERIOD_MINUTES, value);
    }
    //endregion

    //region passportUid
    @Test
    public void testPassportUidShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getPassportUid()).isNull();
    }

    @Test
    public void testPassportUidShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithPassportUid(null).getPassportUid()).isNull();
    }

    @Test
    public void testPassportUidShouldBeExpectedIfDefinedInJson() throws Exception {
        String expected = randomString();
        assertThat(createFiltersWithPassportUid(expected).getPassportUid()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithPassportUid(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.PASSPORT_UID, value);
    }
    //endregion

    //region coordinates
    @Test
    public void testCoordinatesShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getCoordinates()).isNull();
    }

    @Test
    public void testCoordinatesShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithCoordinates(null).getCoordinates()).isNull();
    }

    @Test
    public void testCoordinatesShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithCoordinates(randomString()).getCoordinates()).isNull();
    }

    @Test
    public void testCoordinatesShouldBeNotNullIfDefinedInJson() throws Exception {
        assertThat(createFiltersWithCoordinates(new JSONObject()).getCoordinates()).isNotNull();
    }

    @NonNull
    private Filters createFiltersWithCoordinates(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.COORDINATES, value);
    }
    //endregion

    //region minRecency
    @Test
    public void testMinRecencyShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getMinRecency()).isNull();
    }

    @Test
    public void testMinRecencyShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithMinRecency(null).getMinRecency()).isNull();
    }

    @Test
    public void testMinRecencyShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithMinRecency(randomString()).getMinRecency()).isNull();
    }

    @Test
    public void testMinRecencyShouldBeExpectedIfDefinedInJson() throws Exception {
        long expected = randomLong();
        assertThat(createFiltersWithMinRecency(expected).getMinRecency()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithMinRecency(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.MIN_RECENCY, value);
    }
    //endregion

    //region minAccuracy
    @Test
    public void testMinAccuracyShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getMinAccuracy()).isNull();
    }

    @Test
    public void testMinAccuracyShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithMinAccuracy(null).getMinAccuracy()).isNull();
    }

    @Test
    public void testMinAccuracyShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithMinAccuracy(randomString()).getMinAccuracy()).isNull();
    }

    @Test
    public void testMinAccuracyShouldBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createFiltersWithMinAccuracy(expected).getMinAccuracy()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithMinAccuracy(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.MIN_ACCURACY, value);
    }
    //endregion

    //region isPassiveLocation
    @Test
    public void testIsPassiveLocationShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getPassiveLocation()).isNull();
    }

    @Test
    public void testIsPassiveLocationShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithIsPassiveLocation(null).getPassiveLocation()).isNull();
    }

    @Test
    public void testIsPassiveLocationShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithIsPassiveLocation(randomString()).getPassiveLocation()).isNull();
    }

    @Test
    public void testIsPassiveLocationShouldBeExpectedIfDefinedInJson() throws Exception {
        boolean expected = randomBoolean();
        assertThat(createFiltersWithIsPassiveLocation(expected).getPassiveLocation()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithIsPassiveLocation(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.IS_PASSIVE_LOCATION, value);
    }
    //endregion

    //region minVersionCode
    @Test
    public void testMinVersionCodeShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getMinVersionCode()).isNull();
    }

    @Test
    public void testMinVersionCodeBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithMinVersionCode(null).getMinVersionCode()).isNull();
    }

    @Test
    public void testMinVersionCodeBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithMinVersionCode(randomString()).getMinVersionCode()).isNull();
    }

    @Test
    public void testMinVersionCodeBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createFiltersWithMinVersionCode(expected).getMinVersionCode()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithMinVersionCode(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.MIN_VERSION_CODE, value);
    }
    //endregion

    //region maxVersionCode
    @Test
    public void testMaxVersionCodeShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getMaxVersionCode()).isNull();
    }

    @Test
    public void testMaxVersionCodeShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithMaxVersionCode(null).getMaxVersionCode()).isNull();
    }

    @Test
    public void testMaxVersionCodeShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithMaxVersionCode(randomString()).getMaxVersionCode()).isNull();
    }

    @Test
    public void testMaxVersionCodeShouldBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createFiltersWithMaxVersionCode(expected).getMaxVersionCode()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithMaxVersionCode(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.MAX_VERSION_CODE, value);
    }
    //endregion

    //region minAndroidApiLevel
    @Test
    public void testMinAndroidApiLevelShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getMinAndroidApiLevel()).isNull();
    }

    @Test
    public void testMinAndroidApiLevelBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithMinAndroidApiLevel(null).getMinAndroidApiLevel()).isNull();
    }

    @Test
    public void testMinAndroidApiLevelBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithMinAndroidApiLevel(randomString()).getMinAndroidApiLevel()).isNull();
    }

    @Test
    public void testMinAndroidApiLevelBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createFiltersWithMinAndroidApiLevel(expected).getMinAndroidApiLevel()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithMinAndroidApiLevel(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.MIN_ANDROID_API_LEVEL, value);
    }
    //endregion

    //region maxAndroidApiLevel
    @Test
    public void testMaxAndroidApiLevelShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getMaxAndroidApiLevel()).isNull();
    }

    @Test
    public void testMaxAndroidApiLevelShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithMaxAndroidApiLevel(null).getMaxAndroidApiLevel()).isNull();
    }

    @Test
    public void testMaxAndroidApiLevelShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithMaxAndroidApiLevel(randomString()).getMaxAndroidApiLevel()).isNull();
    }

    @Test
    public void testMaxAndroidApiLevelShouldBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createFiltersWithMaxAndroidApiLevel(expected).getMaxAndroidApiLevel()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithMaxAndroidApiLevel(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.MAX_ANDROID_API_LEVEL, value);
    }
    //endregion

    //region loginFilterType
    @Test
    public void testLoginFilterTypeShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyFilters.getLoginFilterType()).isNull();
    }

    @Test
    public void testLoginFilterTypeBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithLoginFilterType(null).getLoginFilterType()).isNull();
    }

    @Test
    public void testLoginFilterTypeBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createFiltersWithLoginFilterType(randomString()).getLoginFilterType()).isNull();
    }

    @Test
    public void testLoginFilterTypeBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createFiltersWithLoginFilterType(expected).getLoginFilterType()).isEqualTo(expected);
    }

    @NonNull
    private Filters createFiltersWithLoginFilterType(@Nullable Object value) throws Exception {
        return createFiltersWithValues(Constants.PushMessage.Filters.LOGIN_FILTER_TYPE, value);
    }
    //endregion

    //region contentId
    @Test
    public void testContentIdShouldBeNullIfNotDefinedRootElement() {
        assertThat(mEmptyFilters.getContentId()).isNull();
    }

    @Test
    public void testContentIdShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(createFiltersWithValues(randomString(), randomString()).getContentId()).isNull();
    }

    @Test
    public void testContentIdShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createFiltersWithValues(Constants.PushMessage.Filters.CONTENT_ID, null).getContentId()).isNull();
    }

    @Test
    public void testContentIdShouldBeExpectedIfDefinedInJson() throws Exception {
        String expected = randomString();
        assertThat(createFiltersWithValues(Constants.PushMessage.Filters.CONTENT_ID, expected).getContentId())
            .isEqualTo(expected);
    }
    //endregion

    @NonNull
    private static Filters createFiltersWithValues(String key, Object value) throws Exception {
        JSONObject json = new JSONObject();
        json.put(key, value);
        return createFilters(json);
    }

    @NonNull
    private static Filters createFilters(@NonNull JSONObject json) {
        return new Filters(json);
    }
}
