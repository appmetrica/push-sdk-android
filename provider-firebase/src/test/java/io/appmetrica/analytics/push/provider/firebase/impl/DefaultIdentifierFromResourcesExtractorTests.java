package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import android.content.pm.PackageManager;
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

    private static final String DEFAULT_API_KEY_RES_NAME = "google_api_key";
    private static final String DEFAULT_APP_ID_RES_NAME = "google_app_id";
    private static final String DEFAULT_SENDER_ID_RES_NAME = "gcm_defaultSenderId";
    private static final String DEFAULT_PROJECT_ID_RES_NAME = "project_id";

    private Context mContext;
    private String mPackageName;
    private Resources mResources;

    @Before
    public void setUp() throws PackageManager.NameNotFoundException {
        mContext = spy(RuntimeEnvironment.application);
        super.setUp(new DefaultIdentifierFromResourcesExtractor(mContext));

        mPackageName = mContext.getPackageName();
        mResources = mock(Resources.class);
        doReturn(mResources).when(mContext).getResources();
    }

    @NonNull
    @Override
    String createApiKey() {
        String apiKey = randomString();
        int appResId = randomInt();
        doReturn(apiKey).when(mContext).getString(appResId);
        doReturn(appResId).when(mResources).getIdentifier(DEFAULT_API_KEY_RES_NAME, "string", mPackageName);

        return apiKey;
    }

    @NonNull
    @Override
    String createAppId() {
        String appId = randomString();
        int appResId = randomInt();
        doReturn(appId).when(mContext).getString(appResId);
        doReturn(appResId).when(mResources).getIdentifier(DEFAULT_APP_ID_RES_NAME, "string", mPackageName);

        return appId;
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

    @NonNull
    @Override
    String createProjectId() {
        String projectId = randomString();
        int projectResId = randomInt();
        doReturn(projectId).when(mContext).getString(projectResId);
        doReturn(projectResId).when(mResources).getIdentifier(DEFAULT_PROJECT_ID_RES_NAME, "string", mPackageName);

        return projectId;
    }
}
