package io.appmetrica.analytics.push.impl.processing.transform;

import android.content.Context;
import io.appmetrica.analytics.push.model.PushMessage;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class TransformFacadeTest {

    private static final List<Class<? extends TransformProcessor>> transformClasses = Arrays.asList(
        LazyPushTransformProcessor.class,
        FilterTransformProcessor.class
    );

    private Context context;
    private TransformController controller;
    private TransformFacade facade;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        controller = mock(TransformController.class);
        facade = new TransformFacade(context, controller);
    }

    @Test
    public void testCreateDefaultFilters() {
        verify(controller, times(transformClasses.size())).addTransformProcessor(any(TransformProcessor.class));
        for (Class<? extends TransformProcessor> filterClass : transformClasses) {
            verify(controller, times(1)).addTransformProcessor(isA(filterClass));
        }
    }

    @Test
    public void testAddTransformProcessor() {
        TransformProcessor transformProcessor = mock(TransformProcessor.class);
        facade.addTransformProcessor(transformProcessor);
        verify(controller, times(1)).addTransformProcessor(eq(transformProcessor));
    }

    @Test
    public void testTransform() {
        PushMessage pushMessage = mock(PushMessage.class);
        facade.transform(pushMessage);
        verify(controller, times(1)).transform(eq(pushMessage));
    }
}
