package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class CustomIdentifierFromMetaDataExtractorTests extends ExtractorTests {

    private static final String META_DATA_API_KEY = "ymp_firebase_api_key";
    private static final String META_DATA_APP_ID = "ymp_firebase_app_id";
    private static final String META_DATA_SENDER_ID = "ymp_gcm_sender_id";
    private static final String META_DATA_PROJECT_ID = "ymp_firebase_project_id";

    private Context mContext;
    private ApplicationInfo mInfo;

    @Before
    public void setUp() throws PackageManager.NameNotFoundException {
        mContext = spy(RuntimeEnvironment.application);
        super.setUp(new CustomIdentifierFromMetaDataExtractor(mContext));

        mInfo = new ApplicationInfo();
        mInfo.metaData = new Bundle();
        PackageManager pm = mock(PackageManager.class);
        doReturn(mInfo).when(pm).getApplicationInfo(anyString(), anyInt());
        doReturn(pm).when(mContext).getPackageManager();
    }

    @NonNull
    @Override
    String createApiKey() {
        String apiKey = randomString();
        mInfo.metaData.putString(META_DATA_API_KEY, apiKey);

        return apiKey;
    }

    @NonNull
    @Override
    String createAppId() {
        String appId = randomString();
        mInfo.metaData.putString(META_DATA_APP_ID, appId);

        return appId;
    }

    @NonNull
    @Override
    String createSenderId() {
        String senderId = randomString();
        mInfo.metaData.putString(META_DATA_SENDER_ID, "number:" + senderId);

        return senderId;
    }

    @NonNull
    @Override
    String createProjectId() {
        String projectId = randomString();
        mInfo.metaData.putString(META_DATA_PROJECT_ID, projectId);

        return projectId;
    }
}
