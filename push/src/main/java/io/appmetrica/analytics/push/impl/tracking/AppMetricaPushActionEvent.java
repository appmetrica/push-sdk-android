package io.appmetrica.analytics.push.impl.tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import org.json.JSONException;
import org.json.JSONObject;

public class AppMetricaPushActionEvent extends AppMetricaPushEvent {

    private static final String TAG = "[AppMetricaPushActionEvent]";

    protected enum ActionType {
        RECEIVE("receive"),
        DISMISS("dismiss"),
        OPEN("open"),
        CUSTOM("custom"),
        PROCESSED("processed"),
        SHOWN("shown"),
        IGNORED("ignored"),
        EXPIRED("expired"),
        REMOVED("removed"),
        REPLACE("replace");

        @NonNull
        private final String id;

        // do not use annotations since Proguard incorrectly transforms it for enums and Jetifier cannot read it
        ActionType(final String id) {
            this.id = id;
        }

        @NonNull
        private String getId() {
            return id;
        }
    }

    protected static class JsonKeys {
        protected static final String PUSH_ID = "notification_id";
        protected static final String ACTION = "action";

        protected static class Action {
            protected static final String TYPE = "type";
            protected static final String ID = "id";
            protected static final String CATEGORY = "category";
            protected static final String DETAILS = "details";
            protected static final String TEXT = "text";
            protected static final String NEW_PUSH_ID = "new_push_id";
        }
    }

    @NonNull
    private final String pushId;
    @NonNull
    private final Action action;

    private AppMetricaPushActionEvent(@NonNull String pushId, @NonNull String transport, @NonNull Action action) {
        super(EventType.EVENT_NOTIFICATION, transport);
        this.pushId = pushId;
        this.action = action;
    }

    @NonNull
    static AppMetricaPushActionEvent createWithReceiveAction(@NonNull String pushId,
                                                             @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new Action(ActionType.RECEIVE));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithOpenAction(@NonNull String pushId,
                                                          @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new Action(ActionType.OPEN));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithDismissAction(@NonNull String pushId,
                                                             @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new Action(ActionType.DISMISS));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithAdditionalAction(@NonNull String pushId,
                                                                @Nullable String actionId,
                                                                @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new CustomAction(actionId));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithInlineAdditionalAction(@NonNull String pushId,
                                                                      @Nullable String actionId,
                                                                      @NonNull String text,
                                                                      @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new CustomAction(actionId, text));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithProcessedAction(@NonNull final String pushId,
                                                               @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new Action(ActionType.PROCESSED));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithShownAction(@NonNull String pushId,
                                                           @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new Action(ActionType.SHOWN));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithIgnoredAction(@NonNull final String pushId,
                                                             @Nullable final String category,
                                                             @Nullable final String details,
                                                             @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new IgnoredAction(category, details));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithExpiredAction(@NonNull final String pushId,
                                                             @Nullable final String category,
                                                             @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new ExpiredAction(category));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithRemovedAction(@NonNull final String pushId,
                                                             @Nullable final String category,
                                                             @Nullable final String details,
                                                             @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new RemovedAction(category, details));
    }

    @NonNull
    static AppMetricaPushActionEvent createWithReplaceAction(@NonNull final String pushId,
                                                             @Nullable final String newPushId,
                                                             @NonNull String transport) {
        return new AppMetricaPushActionEvent(pushId, transport, new ReplaceAction(newPushId));
    }

    @NonNull
    @Override
    public String getEventValue() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JsonKeys.PUSH_ID, pushId);
            jsonObject.put(JsonKeys.ACTION, action.toJson());
        } catch (JSONException e) {
            DebugLogger.INSTANCE.error(TAG, e, e.getMessage());
        }
        return jsonObject.toString();
    }

    protected static class Action {

        @NonNull
        private final ActionType actionType;

        public Action(@NonNull ActionType actionType) {
            this.actionType = actionType;
        }

        @NonNull
        public JSONObject toJson() throws JSONException {
            return new JSONObject().put(JsonKeys.Action.TYPE, actionType.getId());
        }
    }

    protected static class CustomAction extends Action {

        @Nullable
        private final String actionId;
        @Nullable
        private final String text;

        public CustomAction(@Nullable String actionId) {
            this(actionId, null);
        }

        public CustomAction(@Nullable String actionId, @Nullable String text) {
            super(ActionType.CUSTOM);
            this.actionId = actionId;
            this.text = text;
        }

        @NonNull
        @Override
        public JSONObject toJson() throws JSONException {
            return super.toJson()
                .put(JsonKeys.Action.ID, actionId)
                .put(JsonKeys.Action.TEXT, text);
        }
    }

    protected static class IgnoredAction extends Action {

        @Nullable
        private final String category;
        @Nullable
        private final String details;

        public IgnoredAction(@Nullable String category, @Nullable String details) {
            super(ActionType.IGNORED);
            this.category = category;
            this.details = details;
        }

        @NonNull
        @Override
        public JSONObject toJson() throws JSONException {
            return super.toJson()
                .put(JsonKeys.Action.CATEGORY, category)
                .put(JsonKeys.Action.DETAILS, details);
        }
    }

    protected static class RemovedAction extends Action {

        @Nullable
        private final String category;
        @Nullable
        private final String details;

        public RemovedAction(@Nullable String category, @Nullable String details) {
            super(ActionType.REMOVED);
            this.category = category;
            this.details = details;
        }

        @NonNull
        @Override
        public JSONObject toJson() throws JSONException {
            return super.toJson()
                .put(JsonKeys.Action.CATEGORY, category)
                .put(JsonKeys.Action.DETAILS, details);
        }
    }

    protected static class ExpiredAction extends Action {

        @Nullable
        private final String category;

        public ExpiredAction(@Nullable String category) {
            super(ActionType.EXPIRED);
            this.category = category;
        }

        @NonNull
        @Override
        public JSONObject toJson() throws JSONException {
            return super.toJson()
                .put(JsonKeys.Action.CATEGORY, category);
        }
    }

    protected static class ReplaceAction extends Action {

        @Nullable
        private final String newPushId;

        public ReplaceAction(@Nullable String newPushId) {
            super(ActionType.REPLACE);
            this.newPushId = newPushId;
        }

        @NonNull
        @Override
        public JSONObject toJson() throws JSONException {
            return super.toJson()
                .put(JsonKeys.Action.NEW_PUSH_ID, newPushId);
        }
    }
}
