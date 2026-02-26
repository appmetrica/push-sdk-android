package io.appmetrica.analytics.push.model;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parsed coordinates data.
 */
public class Coordinates {

    private static final int POINT_LATITUDE_INDEX = 0;
    private static final int POINT_LONGITUDE_INDEX = 1;

    @Nullable
    private final Integer radius;
    @Nullable
    private final List<Location> points;

    /**
     * Constructor for {@link Coordinates}.
     *
     * @param jsonObject {@link JSONObject} with ccordinates data
     */
    public Coordinates(@NonNull JSONObject jsonObject) {
        radius = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Filters.Coordinates.RADIUS);
        List<Location> points = extractPointsSafely(jsonObject);
        this.points = points == null ? null : Collections.unmodifiableList(points);
    }

    /**
     * Returns radius.
     * @return radius
     */
    @Nullable
    public Integer getRadius() {
        return radius;
    }

    /**
     * Returns points.
     * @return points
     */
    @Nullable
    public List<Location> getPoints() {
        return points;
    }

    @Nullable
    private List<Location> extractPointsSafely(@NonNull JSONObject jsonObject) {
        if (jsonObject.has(Constants.PushMessage.Filters.Coordinates.POINTS)) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(Constants.PushMessage.Filters.Coordinates.POINTS);
                List<Location> points = new ArrayList<Location>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    points.add(extractLocation(jsonArray.optJSONArray(i)));
                }
                return points;
            } catch (JSONException e) {
                PublicLogger.INSTANCE.error(e, "Error parsing location points");
                TrackersHub.getInstance().reportError("Error parsing location points", e);
            }
        }
        return null;
    }

    @Nullable
    private Location extractLocation(@Nullable JSONArray jsonArray) {
        if (jsonArray != null) {
            try {
                Location location = new Location("");
                location.setLatitude(jsonArray.getDouble(POINT_LATITUDE_INDEX));
                location.setLongitude(jsonArray.getDouble(POINT_LONGITUDE_INDEX));
                return location;
            } catch (JSONException e) {
                PublicLogger.INSTANCE.error(e, "Error parsing location point");
                TrackersHub.getInstance().reportError("Error parsing location point", e);
            }
        }
        return null;
    }
}
