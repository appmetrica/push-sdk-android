package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.util.List;

class PushIdFilter implements PushFilter {

    private static final String NO_PUSH_ID = "PushId is empty";
    private static final String DUPLICATE_PUSH_ID = "Duplicate pushId";

    @NonNull
    private final PushMessageHistory pushMessageHistory;

    public PushIdFilter(@NonNull PushMessageHistory pushMessageHistory) {
        this.pushMessageHistory = pushMessageHistory;
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        String pushId = pushMessage.getNotificationId();
        if (TextUtils.isEmpty(pushId)) {
            return FilterResult.silence(NO_PUSH_ID, null);
        }

        List<String> oldPushIds = pushMessageHistory.getPushIds();
        if (oldPushIds.contains(pushId)) {
            return FilterResult.silence(
                DUPLICATE_PUSH_ID,
                String.format("Push with the same push id [%s] has already been received", pushId)
            );
        }
        return FilterResult.show();
    }
}
