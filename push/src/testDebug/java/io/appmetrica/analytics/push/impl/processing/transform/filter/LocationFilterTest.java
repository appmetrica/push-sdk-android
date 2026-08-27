package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.location.Location;
import io.appmetrica.analytics.push.location.DetailedLocation;
import io.appmetrica.analytics.push.location.LocationProvider;
import io.appmetrica.analytics.push.location.LocationStatus;
import io.appmetrica.analytics.push.location.LocationVerifier;
import io.appmetrica.analytics.push.model.Coordinates;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class LocationFilterTest extends PushFilterTest {

    private final String locationStatusCategory = "locationStatusCategory";
    private final String locationStatusDetails = "locationStatusDetails";

    @Mock
    private PushMessage pushMessage;

    @Mock
    private LocationProvider locationProvider;
    @Mock
    private DetailedLocation detailedLocation;
    @Mock
    private Location location;
    @Mock
    private LocationStatus locationStatus;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(locationProvider.getLocation(anyString(), anyLong(), any(LocationVerifier.class))).thenReturn(detailedLocation);
        when(detailedLocation.getLocation()).thenReturn(location);
        when(detailedLocation.getLocationStatus()).thenReturn(locationStatus);

        when(locationStatus.getCategory()).thenReturn(locationStatusCategory);
        when(locationStatus.getDetails()).thenReturn(locationStatusDetails);
        when(locationStatus.isSuccess()).thenReturn(true);

        Coordinates coordinates = mock(Coordinates.class);
        when(coordinates.getRadius()).thenReturn(null);
        when(coordinates.getPoints()).thenReturn(Collections.singletonList(mock(Location.class)));

        Filters filters = mock(Filters.class);
        when(filters.getMinRecency()).thenReturn(null);
        when(filters.getMinAccuracy()).thenReturn(null);
        when(filters.getPassiveLocation()).thenReturn(null);
        when(filters.getCoordinates()).thenReturn(coordinates);

        PushNotification pushNotification = mock(PushNotification.class);
        when(pushNotification.getChannelId()).thenReturn(null);

        when(pushMessage.getFilters()).thenReturn(filters);
        when(pushMessage.getNotification()).thenReturn(pushNotification);

        super.setUp(new LocationFilter(locationProvider));
    }

    @Test
    public void silenceIfNoFilters() {
        when(pushMessage.getFilters()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void silenceIfNoCoordinates() {
        when(pushMessage.getFilters().getCoordinates()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void silenceIfNoPoints() {
        when(pushMessage.getFilters().getCoordinates().getPoints()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void silenceIfPointsAreEmpty() {
        when(pushMessage.getFilters().getCoordinates().getPoints()).thenReturn(Collections.<Location>emptyList());
        assertShow(pushMessage);
    }

    @Test
    public void silenceIfLocationStatusFromProviderIsNotSuccess() {
        when(locationStatus.isSuccess()).thenReturn(false);
        assertSilence(pushMessage, locationStatusCategory, locationStatusDetails);
    }

    @Test
    public void show() {
        assertShow(pushMessage);
    }
}
