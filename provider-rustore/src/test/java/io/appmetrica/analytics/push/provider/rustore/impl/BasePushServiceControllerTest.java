package io.appmetrica.analytics.push.provider.rustore.impl;

import android.app.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(RobolectricTestRunner.class)
public class BasePushServiceControllerTest {

    @Mock
    private Application application;
    @Mock
    private IdentifierExtractor identifierExtractor;

    private BasePushServiceController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        controller = new BasePushServiceController(application, identifierExtractor);
    }

    @Test
    public void doesNotGetIdentifiersOnCreate() {
        final IdentifierExtractor extractor = mock(IdentifierExtractor.class);
        final BasePushServiceController controller = new BasePushServiceController(application, extractor);
        verifyNoInteractions(extractor);
    }
}
