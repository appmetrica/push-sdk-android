package io.appmetrica.analytics.push.impl.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class UtilsTest {

    @Mock
    private Spanned spanned;

    @Rule
    public final MockedStaticRule<Html> htmlRule = new MockedStaticRule<>(Html.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWrapSoundResIdReturnNullForWrongResId() throws Exception {
        Context ctx = RuntimeEnvironment.application;
        Integer integer = Utils.wrapSoundResId(ctx, "-123");

        assertThat(integer).isNull();
    }

    @Test
    public void testWrapSoundResIdReturnNullForWrongResName() throws Exception {
        Context ctx = RuntimeEnvironment.application;
        Integer integer = Utils.wrapResId(ctx, "bad_name");

        assertThat(integer).isNull();
    }

    @Test
    public void testWrapSoundResIdReturnValidIdForValidSoundId() throws Exception {
        final Context ctx = mock(Context.class);
        final int resId = 54321;

        Resources resources = mock(Resources.class);
        when(resources.openRawResource(resId)).thenReturn(mock(InputStream.class));
        when(ctx.getResources()).thenReturn(resources);

        Integer integer = Utils.wrapSoundResId(ctx, String.valueOf(resId));

        assertThat(integer).isEqualTo(resId);
    }

    @Test
    public void testWrapSoundResIdReturnValidIdForValidSoundName() throws Exception {
        final Context ctx = mock(Context.class);
        final String resName = "beep";
        final int resId = 54321;

        Resources resources = mock(Resources.class);
        when(resources.openRawResource(resId)).thenThrow(new Resources.NotFoundException());
        when(ctx.getResources()).thenReturn(resources);
        when(resources.getIdentifier(resName, "raw", ctx.getPackageName())).thenReturn(resId);

        Integer integer = Utils.wrapSoundResId(ctx, resName);

        assertThat(integer).isEqualTo(resId);
    }

    @Test
    public void testWrapResIdReturnNullForWrongResId() throws Exception {
        Context ctx = RuntimeEnvironment.application;
        Integer integer = Utils.wrapResId(ctx, "-123");

        assertThat(integer).isNull();
    }

    @Test
    public void testWrapResIdReturnNullForWrongResName() throws Exception {
        Context ctx = RuntimeEnvironment.application;
        Integer integer = Utils.wrapResId(ctx, "bad_name");

        assertThat(integer).isNull();
    }

    @Test
    public void testWrapResIdReturnValidIdForValidDrawableName() throws Exception {
        final Context ctx = spy(RuntimeEnvironment.application);
        final int resId = 54321;

        Resources resources = mock(Resources.class);
        doReturn(mock(Drawable.class)).when(resources).getDrawable(resId, null);
        doReturn(resources).when(ctx).getResources();

        int integer = Utils.wrapResId(ctx, String.valueOf(resId));

        assertThat(integer).isEqualTo(resId);
    }

    @Test
    @Config(sdk = 16)
    public void testWrapResIdReturnValidIdForValidDrawableNameOldApi() throws Exception {
        final Context ctx = spy(RuntimeEnvironment.application);
        final int resId = 54321;

        Resources resources = mock(Resources.class);
        doReturn(resources).when(ctx).getResources();
        when(resources.getDrawable(resId)).thenReturn(mock(Drawable.class));

        int integer = Utils.wrapResId(ctx, String.valueOf(resId));

        assertThat(integer).isEqualTo(resId);
    }

    @Test
    public void testWrapResIdForDrawableResName() throws Exception {
        Context ctx = spy(RuntimeEnvironment.application);
        String resName = "icon";
        final int expected = 123;

        Resources resources = mock(Resources.class);
        doReturn(resources).when(ctx).getResources();
        when(resources.getIdentifier(resName, "drawable", ctx.getPackageName())).thenReturn(expected);

        int integer = Utils.wrapResId(ctx, resName);

        assertThat(integer).isEqualTo(expected);
    }

    @Test
    public void testWrapResIdForMipmapResName() throws Exception {
        Context ctx = spy(RuntimeEnvironment.application);
        String resName = "mapmap_icon";
        final int expected = 123;

        Resources resources = mock(Resources.class);
        doReturn(resources).when(ctx).getResources();
        when(resources.getIdentifier(resName, "mipmap", ctx.getPackageName())).thenReturn(expected);

        int integer = Utils.wrapResId(ctx, resName);

        assertThat(integer).isEqualTo(expected);
    }

    @Test
    public void testGetStringFromMetaData() throws PackageManager.NameNotFoundException {
        final String name = "name";
        final String value = "value";

        ApplicationInfo info = new ApplicationInfo();
        info.metaData = new Bundle();
        info.metaData.putString(name, value);
        PackageManager pm = mock(PackageManager.class);
        doReturn(info).when(pm).getApplicationInfo(anyString(), anyInt());
        Context ctx = spy(RuntimeEnvironment.application);
        doReturn(pm).when(ctx).getPackageManager();

        assertThat(CoreUtils.getStringFromMetaData(ctx, name)).isEqualTo(value);
    }

    @Test
    public void testGetStringFromMetaDataReturnNullForWrongName() throws PackageManager.NameNotFoundException {
        ApplicationInfo info = new ApplicationInfo();
        PackageManager pm = mock(PackageManager.class);
        doReturn(info).when(pm).getApplicationInfo(anyString(), anyInt());
        Context ctx = spy(RuntimeEnvironment.application);
        doReturn(pm).when(ctx).getPackageManager();

        assertThat(CoreUtils.getStringFromMetaData(ctx, "")).isNull();
    }

    @Test
    public void testGetIntegerFromMetaData() throws PackageManager.NameNotFoundException {
        final String name = "name";
        final Integer value = 42;

        ApplicationInfo info = new ApplicationInfo();
        info.metaData = new Bundle();
        info.metaData.putInt(name, value);
        PackageManager pm = mock(PackageManager.class);
        doReturn(info).when(pm).getApplicationInfo(anyString(), anyInt());
        Context ctx = spy(RuntimeEnvironment.application);
        doReturn(pm).when(ctx).getPackageManager();

        assertThat(Utils.getIntegerFromMetaData(ctx, name)).isEqualTo(value);
    }

    @Test
    public void testGetIntegerFromMetaDataReturnNullForWrongName() throws PackageManager.NameNotFoundException {
        ApplicationInfo info = new ApplicationInfo();
        PackageManager pm = mock(PackageManager.class);
        doReturn(info).when(pm).getApplicationInfo(anyString(), anyInt());
        Context ctx = spy(RuntimeEnvironment.application);
        doReturn(pm).when(ctx).getPackageManager();

        assertThat(Utils.getIntegerFromMetaData(ctx, "")).isNull();
    }

    @Test
    public void testGetStringFromResources() {
        Context ctx = spy(RuntimeEnvironment.application);

        int resId = 666;
        final String name = "name";
        final String value = "value";
        String packageName = ctx.getPackageName();

        Resources resources = mock(Resources.class);
        doReturn(resId).when(resources).getIdentifier(name, "string", packageName);
        doReturn(resources).when(ctx).getResources();
        doReturn(value).when(ctx).getString(resId);

        assertThat(CoreUtils.getStringFromResources(ctx, name)).isEqualTo(value);
    }

    @Test
    public void testGetStringFromResourcesForWrongName() {
        Context ctx = spy(RuntimeEnvironment.application);

        assertThat(CoreUtils.getStringFromResources(ctx, "qwerty")).isNull();
    }

    @Test
    public void testFromMapToBundle() {
        String key = "key";
        String value = "val";

        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);

        Bundle bundle = CoreUtils.fromMapToBundle(map);

        assertThat(bundle.isEmpty()).isFalse();
        assertThat(bundle.getString(key)).isEqualTo(value);
    }

    @Test
    public void testFromMapToBundleIfNull() {
        assertThat(CoreUtils.fromMapToBundle(null).isEmpty()).isTrue();
    }

    @Test
    public void getExpectedValue() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertThat(Utils.getOrDefault(o1, o2)).isSameAs(o1);
    }

    @Test
    public void getDefaultValue() {
        Object o1 = null;
        Object o2 = new Object();
        assertThat(Utils.getOrDefault(o1, o2)).isSameAs(o2);
    }

    @Test
    public void testExtractPushIdIfPushIdIsDefined() throws JSONException {
        String pushId = "push_id";
        Bundle bundle = new Bundle();
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, new JSONObject()
            .put(Constants.PushMessage.NOTIFICATION_ID, pushId)
            .toString());
        assertThat(Utils.extractPushId(bundle)).isEqualTo(pushId);
    }

    @Test
    public void testExtractPushIdIfPushIdIsNotDefined() {
        Bundle bundle = new Bundle();
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, new JSONObject()
            .toString());
        assertThat(Utils.extractPushId(bundle)).isNull();
    }

    @Test
    public void testExtractPushIdIfRootElementIsNotDefined() {
        assertThat(Utils.extractPushId(new Bundle())).isNull();
    }

    @Test
    public void testExtractPushIdIfRootElementIsNotJson() {
        Bundle bundle = new Bundle();
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, "some text");
        assertThat(Utils.extractPushId(bundle)).isNull();
    }

    @Test
    public void wrapHtml() {
        final String value = randomString();

        when(Html.fromHtml(value)).thenReturn(spanned);

        assertThat(Utils.wrapHtml(value)).isEqualTo(spanned);
    }
}
