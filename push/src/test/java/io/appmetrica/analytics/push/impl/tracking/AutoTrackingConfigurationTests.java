package io.appmetrica.analytics.push.impl.tracking;

import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AutoTrackingConfigurationTests {

    private AutoTrackingConfiguration.Builder builder;

    @Before
    public void setUp() {
        builder = AutoTrackingConfiguration.newBuilder();
    }

    @Test
    public void testDefaultTrackingReceiveAction() {
        assertThat(builder.build().trackingReceiveAction).isTrue();
    }

    @Test
    public void testDefaultTrackingDismissAction() {
        assertThat(builder.build().trackingDismissAction).isTrue();
    }

    @Test
    public void testTrackingOpenAction() {
        assertThat(builder.disableTrackingOpenAction().build().trackingOpenAction).isFalse();
    }

    @Test
    public void testDefaultTrackingOpenAction() {
        assertThat(builder.build().trackingOpenAction).isTrue();
    }

    @Test
    public void testTrackingAllAdditionalAction() {
        assertThat(builder.disableTrackingAllAdditionalAction().build().trackingAllAdditionalAction).isFalse();
    }

    @Test
    public void testDefaultTrackingAllAdditionalAction() {
        assertThat(builder.build().trackingAllAdditionalAction).isTrue();
    }

    @Test
    public void testDefaultTrackingProcessedAction() {
        assertThat(builder.build().trackingProcessedAction).isTrue();
    }

    @Test
    public void testTrackingAdditionalAction() {
        final String actionId = new RandomStringGenerator().nextString();
        AutoTrackingConfiguration autoTrackingConfiguration = builder.disableTrackingAdditionalAction(actionId).build();
        assertThat(autoTrackingConfiguration.disabledActionIdSet.size()).isEqualTo(1);
        assertThat(autoTrackingConfiguration.disabledActionIdSet).contains(actionId);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrackingAdditionalActionNotChange() {
        AutoTrackingConfiguration autoTrackingConfiguration = builder.build();
        autoTrackingConfiguration.disabledActionIdSet.add(new RandomStringGenerator().nextString());
    }

    @Test
    public void testTrackingAdditionalActionIdIfAllAdditionalActionNotDisabledAndActionIdNotDisabled() {
        AutoTrackingConfiguration autoTrackingConfiguration = builder.build();
        assertThat(autoTrackingConfiguration.isTrackingAdditionalAction(new RandomStringGenerator().nextString()))
            .isTrue();
    }

    @Test
    public void testTrackingAdditionalActionIdIfAllAdditionalActionDisabledAndActionIdNotDisabled() {
        AutoTrackingConfiguration autoTrackingConfiguration = builder.disableTrackingAllAdditionalAction().build();
        assertThat(autoTrackingConfiguration.isTrackingAdditionalAction(new RandomStringGenerator().nextString()))
            .isFalse();
    }

    @Test
    public void testTrackingAdditionalActionIdIfAllAdditionalActionNotDisabledAndActionIdDisabled() {
        final String actionId = new RandomStringGenerator().nextString();
        final String otherActionId = new RandomStringGenerator().nextString();
        AutoTrackingConfiguration autoTrackingConfiguration = builder.disableTrackingAdditionalAction(actionId).build();
        assertThat(autoTrackingConfiguration.isTrackingAdditionalAction(actionId)).isFalse();
        assertThat(autoTrackingConfiguration.isTrackingAdditionalAction(otherActionId)).isTrue();
    }

    @Test
    public void testTrackingAdditionalActionIdIfAllAdditionalActionDisabledAndActionIdDisabled() {
        final String actionId = new RandomStringGenerator().nextString();
        final String otherActionId = new RandomStringGenerator().nextString();
        AutoTrackingConfiguration autoTrackingConfiguration = builder
            .disableTrackingAllAdditionalAction()
            .disableTrackingAdditionalAction(actionId)
            .build();
        assertThat(autoTrackingConfiguration.isTrackingAdditionalAction(actionId)).isFalse();
        assertThat(autoTrackingConfiguration.isTrackingAdditionalAction(otherActionId)).isFalse();
    }
}
