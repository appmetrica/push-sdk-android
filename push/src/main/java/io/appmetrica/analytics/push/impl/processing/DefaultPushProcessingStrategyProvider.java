package io.appmetrica.analytics.push.impl.processing;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.processing.strategy.GeneralPushStrategy;
import io.appmetrica.analytics.push.impl.processing.strategy.PushStrategy;
import io.appmetrica.analytics.push.impl.processing.strategy.RemovingSilentPushStrategy;
import io.appmetrica.analytics.push.impl.processing.strategy.SilentPushStrategy;
import io.appmetrica.analytics.push.model.PushMessage;

public class DefaultPushProcessingStrategyProvider implements PushProcessingStrategyProvider {

    @Override
    public PushStrategy getPushStrategy(@NonNull PushMessage pushMessage) {
        if (pushMessage.isSilent()) {
            if (CoreUtils.isEmpty(pushMessage.getPushIdToRemove())) {
                return new SilentPushStrategy();
            } else {
                return new RemovingSilentPushStrategy();
            }
        } else {
            if (pushMessage.getNotification() != null) {
                return new GeneralPushStrategy();
            }
        }
        return null;
    }
}
