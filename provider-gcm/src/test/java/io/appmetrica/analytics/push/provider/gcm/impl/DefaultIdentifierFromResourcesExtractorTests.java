package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class DefaultIdentifierFromResourcesExtractorTests extends ExtractorTests {

    private static final String DEFAULT_SENDER_ID_RES_NAME = "gcm_defaultSenderId";

    private Context mContext;
    private String mPackageName;
    private Resources mResources;

    @Before
    public void setUp() {
        mContext = spy(RuntimeEnvironment.application);
        super.setUp(new DefaultIdentifierFromResourcesExtractor(mContext));

        mPackageName = mContext.getPackageName();
        mResources = mock(Resources.class);
        doReturn(mResources).when(mContext).getResources();
    }

    @NonNull
    @Override
    String createSenderId() {
        String senderId = randomString();
        int senderResId = randomInt();
        doReturn(senderId).when(mContext).getString(senderResId);
        doReturn(senderResId).when(mResources).getIdentifier(DEFAULT_SENDER_ID_RES_NAME, "string", mPackageName);

        return senderId;
    }
}
