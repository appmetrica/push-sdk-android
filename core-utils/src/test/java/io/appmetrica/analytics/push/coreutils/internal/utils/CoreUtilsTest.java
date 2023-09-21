package io.appmetrica.analytics.push.coreutils.internal.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
public class CoreUtilsTest {

    @Mock
    private PackageManager manager;
    @Mock
    private ApplicationInfo applicationInfo;
    @Mock
    private Context context;
    private String packageName = "io.appmetrica.analytics.push.text";
    private Bundle metaData = new Bundle();

    @Before
    public void setUp() throws PackageManager.NameNotFoundException {
        MockitoAnnotations.initMocks(this);
        doReturn(manager).when(context).getPackageManager();
        doReturn(packageName).when(context).getPackageName();
        doReturn(applicationInfo).when(manager).getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        applicationInfo.metaData = metaData;
    }

    @Test
    public void testInt() {
        String key = "key";
        metaData.putInt(key, 100);
        assertThat(CoreUtils.getStringOrOtherFromMetaData(context, key)).isEqualTo("100");
    }

    @Test
    public void testString() {
        String key = "key";
        metaData.putString(key, "100");
        assertThat(CoreUtils.getStringOrOtherFromMetaData(context, key)).isEqualTo("100");
    }

    @Test
    public void testLong() {
        String key = "key";
        metaData.putLong(key, 100L);
        assertThat(CoreUtils.getStringOrOtherFromMetaData(context, key)).isEqualTo("100");
    }

    @Test
    public void testIsEmpty() {
        assertTrue("null should be empty", CoreUtils.isEmpty(null));
        assertTrue("empty string should be empty", CoreUtils.isEmpty(""));
        assertFalse("any string should not be empty", CoreUtils.isEmpty(randomString()));
    }

    @Test
    public void testIsNotEmpty() {
        assertFalse("null should be empty", CoreUtils.isNotEmpty(null));
        assertFalse("empty string should be empty", CoreUtils.isNotEmpty(""));
        assertTrue("any string should not be empty", CoreUtils.isNotEmpty(randomString()));
    }

    @Test
    public void testExtractRootElementIfPushMessageRootDoesNotExist() {
        assertThat(CoreUtils.extractRootElement(new Bundle())).isNull();
    }

    @Test
    public void testNotificationIsNotRelatedToSdkIfPushMessageRootDoesNotExist() {
        assertThat(CoreUtils.isNotificationRelatedToSDK(new Bundle())).isFalse();
    }

    @Test
    public void testNotificationIsRelatedToSdkIfPushMessageRootExists() {
        Bundle bundle = new Bundle();
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, new JSONObject().toString());
        assertThat(CoreUtils.isNotificationRelatedToSDK(bundle)).isTrue();
    }
}
