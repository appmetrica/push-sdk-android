package io.appmetrica.analytics.push.model;

import android.content.Context;
import android.content.res.Resources;
import io.appmetrica.analytics.push.impl.utils.BitmapLoader;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Random;
import org.json.JSONObject;
import org.junit.Before;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class PushNotificationTestsData {
    protected Context mContext;
    protected Resources mResources;
    protected PushNotification mEmptyPushNotification;
    protected BitmapLoader mBitmapLoader;

    @Before
    public void setUp() {
        mContext = spy(RuntimeEnvironment.application.getApplicationContext());
        mResources = mock(Resources.class);
        when(mContext.getResources()).thenReturn(mResources);
        mEmptyPushNotification = createPushNotification(new JSONObject());
        mBitmapLoader = mock(BitmapLoader.class);
    }

    protected static String randomString() {
        return new RandomStringGenerator(new Random().nextInt(1000) + 1).nextString().concat("_");
    }

    protected static int randomNegativeInt() {
        return new Random().nextInt(10000) - 10001;
    }

    protected static int randomPositiveInt() {
        return new Random().nextInt(10000) + 1;
    }

    protected PushNotification createPushNotification(JSONObject jsonObject) {
        return new PushNotification(mContext, jsonObject, mBitmapLoader);
    }
}
