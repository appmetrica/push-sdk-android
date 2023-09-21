package io.appmetrica.analytics.push.impl.processing.transform;

import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class TransformResultTest {

    @Test
    public void testSuccess() {
        PushMessage pushMessage = mock(PushMessage.class);
        TransformResult result = TransformResult.success(pushMessage);
        assertThat(result.pushMessage).isEqualTo(pushMessage);
        assertThat(result.filterResult).isEqualTo(PushFilter.FilterResult.show());
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void testFailure() {
        PushMessage pushMessage = mock(PushMessage.class);
        String category = randomString();
        String details = randomString();
        TransformResult result = TransformResult.failure(pushMessage, category, details);
        assertThat(result.pushMessage).isEqualTo(pushMessage);
        assertThat(result.filterResult.filterResultCode).isEqualTo(PushFilter.FilterResultCode.SILENCE);
        assertThat(result.filterResult.category).isEqualTo(category);
        assertThat(result.filterResult.details).isEqualTo(details);
        assertThat(result.isSuccess()).isFalse();
    }
}
