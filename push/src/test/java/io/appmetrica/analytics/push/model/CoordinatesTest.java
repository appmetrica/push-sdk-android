package io.appmetrica.analytics.push.model;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.Constants;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class CoordinatesTest {

    protected Coordinates mEmptyCoordinates;

    @Before
    public void setUp() {
        mEmptyCoordinates = createCoordinates(new JSONObject());
    }

    //region radius
    @Test
    public void testRadiusShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyCoordinates.getRadius()).isNull();
    }

    @Test
    public void testRadiusShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createCoordinatesWithRadius(null).getRadius()).isNull();
    }

    @Test
    public void testRadiusShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createCoordinatesWithRadius(randomString()).getRadius()).isNull();
    }

    @Test
    public void testRadiusShouldBeExpectedIfDefinedInJson() throws Exception {
        int expected = randomInt();
        assertThat(createCoordinatesWithRadius(expected).getRadius()).isEqualTo(expected);
    }

    @NonNull
    private Coordinates createCoordinatesWithRadius(@Nullable Object value) throws Exception {
        return createCoordinatesWithValues(Constants.PushMessage.Filters.Coordinates.RADIUS, value);
    }
    //endregion

    //region points
    @Test
    public void testPointsShouldBeNullIfNotDefinedInJson() {
        assertThat(mEmptyCoordinates.getPoints()).isNull();
    }

    @Test
    public void testPointsShouldBeNullIfNullDefinedInJson() throws Exception {
        assertThat(createCoordinatesWithPoints(null).getPoints()).isNull();
    }

    @Test
    public void testPointsShouldBeNullIfInvalidValueDefinedInJson() throws Exception {
        assertThat(createCoordinatesWithPoints(randomString()).getPoints()).isNull();
    }

    @Test
    public void testPointsShouldContainsOnlyNullIfInvalidValueDefinedInJsonArray() throws Exception {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(randomString()).put(randomInt()).put(null);
        Coordinates coordinates = createCoordinatesWithPoints(jsonArray);
        assertThat(coordinates.getPoints()).hasSize(jsonArray.length());
        assertThat(coordinates.getPoints()).containsOnly((Location) null);
    }

    @Test
    public void testPointsShouldBeExpectedIfDefinedInJson() throws Exception {
        double latitude = new Random().nextDouble();
        double longitude = new Random().nextDouble();
        JSONArray jsonArray = new JSONArray().put(createLocationJson(latitude, longitude));
        List<Location> points = createCoordinatesWithPoints(jsonArray).getPoints();
        assertThat(points).hasSize(jsonArray.length());
        assertThat(points.get(0).getLatitude()).isEqualTo(latitude);
        assertThat(points.get(0).getLongitude()).isEqualTo(longitude);
    }

    @Test
    public void testPointShouldBeNullIfInvalidDefinedInJson() throws Exception {
        JSONArray pointJson = new JSONArray().put(randomString()).put(randomString());
        JSONArray pointsJson = new JSONArray().put(pointJson);
        List<Location> points = createCoordinatesWithPoints(pointsJson).getPoints();
        assertThat(points).hasSize(pointsJson.length());
        assertThat(points.get(0)).isNull();
    }

    @NonNull
    public JSONArray createLocationJson(double latitude, double longitude) throws JSONException {
        return new JSONArray().put(latitude).put(longitude);
    }

    @NonNull
    private Coordinates createCoordinatesWithPoints(@Nullable Object value) throws Exception {
        return createCoordinatesWithValues(Constants.PushMessage.Filters.Coordinates.POINTS, value);
    }
    //endregion

    @NonNull
    private Coordinates createCoordinatesWithValues(String key, Object value) throws Exception {
        JSONObject json = new JSONObject();
        json.put(key, value);
        return createCoordinates(json);
    }

    @NonNull
    private Coordinates createCoordinates(@NonNull JSONObject json) {
        return new Coordinates(json);
    }
}
