package io.appmetrica.analytics.push.coreutils.internal;

public class CoreConstants {

    public static final String EXTRA_TRANSPORT = "io.appmetrica.analytics.push.extra.TRANSPORT";
    public static final String MIN_PROCESSING_DELAY = "io.appmetrica.analytics.push.extra.MIN_PROCESSING_DELAY";

    public static class Transport {

        public static final String FIREBASE = "firebase";
        public static final String GCM = "gcm";
        public static final String HMS = "hms";
        public static final String RUSTORE = "rustore";

        public static final String UNKNOWN = "unknown";

    }

    public static class PushMessage {

        public static final String ROOT_ELEMENT = "yamp";
        public static final String SERVICE_TYPE = "i";
        public static final String PROCESSING_MIN_TIME = "j";
    }

    public static final String LINK_TO_INTEGRATION_PUSH_SDK =
        "https://appmetrica.io/docs/mobile-sdk-dg/push/android-initialize.html";

    public static final String EXCEPTION_MESSAGE_ERROR_ACTIVATE = "AppMetrica Push SDK is not activated correctly. " +
        "Please, activate in accordance with the documentation: " + LINK_TO_INTEGRATION_PUSH_SDK;

}
