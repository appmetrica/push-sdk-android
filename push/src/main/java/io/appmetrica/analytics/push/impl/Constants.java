package io.appmetrica.analytics.push.impl;

import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;

public class Constants {
    public static final String SILENT_PUSH_POSTFIX = ".action.ymp.SILENT_PUSH_RECEIVE";
    public static final String INLINE_PUSH_POSTFIX = ".action.ymp.INLINE_PUSH_RECEIVE";
    public static final String PUSH_ID = "io.appmetrica.analytics.push.extra.PUSH_ID";
    public static final String INLINE_ACTION_REPLY = "io.appmetrica.analytics.push.extra.INLINE_ACTION_REPLY";
    public static final String NOTIFICATION_ID = "io.appmetrica.analytics.push.extra.NOTIFICATION_ID";
    public static final String NOTIFICATION_TAG = "io.appmetrica.analytics.push.extra.NOTIFICATION_TAG";
    public static final String PAYLOAD = "io.appmetrica.analytics.push.extra.PAYLOAD";
    public static final String DEFAULT_ICON_META_DATA_NAME = "io.appmetrica.analytics.push.default_notification_icon";

    public static class PushMessage {

        public static class Notification {

            public static class LedLights {
                public static final String LED_LIGHTS_COLOR = "a";
                public static final String LED_LIGHTS_ON_MS = "b";
                public static final String LED_LIGHTS_OFF_MS = "c";
            }

            public static class AdditionalAction {
                public static final String ID = "a";
                public static final String TITLE = "b";
                public static final String ACTION_URL = "c";
                public static final String ICON_RES_ID = "d";
                public static final String HIDE_QUICK_CONTROL_PANEL = "e";
                public static final String AUTO_CANCEL = "f";
                public static final String EXPLICIT_INTENT = "g";
                public static final String TYPE = "h";
                public static final String LABEL = "i";
                public static final String HIDE_AFTER_SECONDS = "j";
                public static final String OPEN_TYPE = "k";
                public static final String USE_ACTIVITY_NEW_TASK_FLAG = "l";
            }

            public static final String ID = "a";
            public static final String AUTO_CANCEL = "c";
            public static final String CATEGORY = "b";
            public static final String COLOR = "d";
            public static final String CONTENT_TITLE = "e";
            public static final String CONTENT_INFO = "f";
            public static final String CONTENT_TEXT = "g";
            public static final String CONTENT_SUBTEXT = "h";
            public static final String TICKER = "i";
            public static final String DEFAULTS = "j";
            public static final String GROUP = "k";
            public static final String GROUP_SUMMARY = "l";
            public static final String LED_LIGHTS = "m";
            public static final String DISPLAYED_NUMBER = "n";
            public static final String ONGOING = "o";
            public static final String ONLY_ALERT_ONCE = "p";
            public static final String PRIORITY = "q";
            public static final String WHEN = "r";
            public static final String SHOW_WHEN = "s";
            public static final String SORT_KEY = "t";
            public static final String VIBRATE = "u";
            public static final String VISIBILITY = "v";
            public static final String OPEN_ACTION = "w";
            public static final String ICON_RES_ID = "x";
            public static final String LARGE_ICON_URL = "y";
            public static final String ADDITIONAL_ACTIONS = "z";
            public static final String LARGE_BITMAP_URL = "aa";
            public static final String SOUND_ENABLED = "ab";
            public static final String CHANNEL_ID = "ac";
            public static final String EXPLICIT_INTENT = "ad";
            public static final String LARGE_ICON_ID = "ae";
            public static final String LARGE_BITMAP_ID = "af";
            public static final String TAG = "ag";
            public static final String NOTIFICATION_TTL = "ah";
            public static final String SOUND_RES_ID = "ai";
            public static final String TIME_TO_HIDE_MILLIS = "aj";
            public static final String USE_ACTIVITY_NEW_TASK_FLAG = "ak";
            public static final String OPEN_TYPE = "al";
        }

        public static class Filters {
            public static final String MIN_ACCURACY = "a";
            public static final String COORDINATES = "c";
            public static final String MAX_PUSH_PER_DAY = "d";
            public static final String IS_PASSIVE_LOCATION = "m";
            public static final String ONE_PUSH_PER_PERIOD_MINUTES = "p";
            public static final String MIN_RECENCY = "r";
            public static final String MIN_ANDROID_API_LEVEL = "s";
            public static final String MAX_ANDROID_API_LEVEL = "t";
            public static final String PASSPORT_UID = "u";
            public static final String MIN_VERSION_CODE = "v";
            public static final String MAX_VERSION_CODE = "W";
            public static final String LOGIN_FILTER_TYPE = "x";
            public static final String CONTENT_ID = "i";

            public static class Coordinates {
                public static final String POINTS = "p";
                public static final String RADIUS = "r";
            }
        }

        public static class LazyPush {

            public static class LocationRequestInfo {
                public static final String PROVIDER = "a";
                public static final String REQUEST_TIMEOUT_SECONDS = "b";
                public static final String MIN_RECENCY = "c";
                public static final String MIN_ACCURACY = "d";
            }

            public static final String URL = "a";
            public static final String USE_CUR_PUSH_AS_FALLBACK = "b";
            public static final String HEADERS = "c";
            public static final String LOCATION_REQUEST_INFO = "d";
            public static final String RETRY_STRATEGY_SECONDS = "e";
        }

        public static final String FIREBASE_TOKEN = "to";
        public static final String PRIORITY = "priority";
        public static final String ROOT_ELEMENT = "yamp";
        public static final String NOTIFICATION_ID = "a";
        public static final String SILENT = "b";
        public static final String PAYLOAD = "c";
        public static final String NOTIFICATION = "d";
        public static final String PUSH_ID_TO_REMOVE = "e";
        public static final String FILTERS = "f";
        public static final String LAZY_PUSH = "g";
        public static final String TIME_TO_SHOW_MILLIS = "h";
        public static final String SERVICE_TYPE = "i";
    }

    public static final float DEFAULT_NOTIFICATION_LARGE_ICON_WIDTH = 64f;
    public static final float DEFAULT_NOTIFICATION_LARGE_ICON_HEIGHT = 64f;

    public static final String EXCEPTION_MESSAGE_METRICA_IS_NOT_ACTIVATED = "AppMetrica isn't initialized. " +
        "Use AppMetrica#activate(android.content.Context, String) method to activate. " +
        "See more at " + CoreConstants.LINK_TO_INTEGRATION_PUSH_SDK;

    public static final String IGNORED_CATEGORY_PUSH_DATA_FORMAT_IS_INVALID = "Push data format is invalid";
    public static final String IGNORED_CATEGORY_CHANNEL_IS_MISSING = "Notification channel is missing";
    public static final String IGNORED_CATEGORY_NOTIFICATION_IS_NULL = "Notification is null";
}
