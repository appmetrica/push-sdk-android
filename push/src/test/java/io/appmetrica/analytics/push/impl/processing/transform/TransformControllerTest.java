package io.appmetrica.analytics.push.impl.processing.transform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.PushMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TransformControllerTest extends TransformProcessorTest {

    private PushMessage pushMessage;
    private String category;
    private String details;
    private TransformController controller;

    @Before
    public void setUp() {
        pushMessage = mock(PushMessage.class);
        category = randomString();
        details = randomString();
        controller = new TransformController();
        super.setUp(controller);
    }

    @Test
    public void testAllTransformSuccess() {
        controller.addTransformProcessor(createTransformWithSuccessResult(pushMessage));
        controller.addTransformProcessor(createTransformWithSuccessResult(pushMessage));
        assertSuccess(pushMessage);
    }

    @Test
    public void testFirstFilterSilence() {
        controller.addTransformProcessor(createTransformWithFailureResult(pushMessage, category, details));
        controller.addTransformProcessor(createTransformWithSuccessResult(pushMessage));
        assertFailure(pushMessage, category, details);
    }

    @Test
    public void testLastFilterSilence() {
        controller.addTransformProcessor(createTransformWithSuccessResult(pushMessage));
        controller.addTransformProcessor(createTransformWithFailureResult(pushMessage, category, details));
        assertFailure(pushMessage, category, details);
    }

    @Test
    public void testAllFiltersSilence() {
        controller.addTransformProcessor(createTransformWithFailureResult(pushMessage, category, details));
        controller.addTransformProcessor(createTransformWithFailureResult(pushMessage, randomString(), randomString()));
        assertFailure(pushMessage, category, details);
    }

    @Test
    public void testTransformPushMessageForSuccess() {
        PushMessage expectedPushMessage = mock(PushMessage.class);
        controller.addTransformProcessor(createTransformWithSuccessResult(expectedPushMessage));
        assertSuccess(pushMessage, expectedPushMessage);
    }

    @Test
    public void testTransformPushMessageForFailure() {
        PushMessage expectedPushMessage = mock(PushMessage.class);
        controller.addTransformProcessor(createTransformWithFailureResult(expectedPushMessage, category, details));
        assertFailure(pushMessage, expectedPushMessage, category, details);
    }

    @Test
    public void testUseNewPushMessageForSecondTransform() {
        PushMessage pushMessageAfterFirstTransform = mock(PushMessage.class);
        PushMessage expectedPushMessage = mock(PushMessage.class);
        controller.addTransformProcessor(createTransformWithSuccessResult(pushMessageAfterFirstTransform));
        TransformProcessor transformProcessor = mock(TransformProcessor.class);
        when(transformProcessor.transform(any(PushMessage.class)))
            .thenReturn(TransformResult.failure(pushMessage, randomString(), randomString()));
        when(transformProcessor.transform(pushMessageAfterFirstTransform))
            .thenReturn(TransformResult.success(expectedPushMessage));
        controller.addTransformProcessor(transformProcessor);
        assertSuccess(expectedPushMessage);
    }

    @NonNull
    private TransformProcessor createTransformWithSuccessResult(@NonNull PushMessage newPushMessage) {
        TransformProcessor transformProcessor = mock(TransformProcessor.class);
        when(transformProcessor.transform(any(PushMessage.class))).thenReturn(TransformResult.success(newPushMessage));
        return transformProcessor;
    }

    @NonNull
    private TransformProcessor createTransformWithFailureResult(
        @NonNull PushMessage newPushMessage,
        @Nullable String category,
        @Nullable String details
    ) {
        TransformProcessor transformProcessor = mock(TransformProcessor.class);
        when(transformProcessor.transform(any(PushMessage.class)))
            .thenReturn(TransformResult.failure(newPushMessage, category, details));
        return transformProcessor;
    }
}
