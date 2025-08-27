package io.appmetrica.analytics.push;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.event.PushEvent;
import io.appmetrica.analytics.push.event.PushEventListener;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.NotificationCustomizersHolderProvider;
import io.appmetrica.analytics.push.impl.PushNotificationFactoryProvider;
import io.appmetrica.analytics.push.impl.event.PushEventListenerWrapper;
import io.appmetrica.analytics.push.impl.lazypush.LazyPushTransformRuleProviderHolder;
import io.appmetrica.analytics.push.impl.location.LocationProviderHolder;
import io.appmetrica.analytics.push.impl.token.TokenManager;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerWrapper;
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRuleProvider;
import io.appmetrica.analytics.push.location.LocationProvider;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import io.appmetrica.analytics.push.notification.NotificationCustomizersHolder;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;
import io.appmetrica.analytics.push.provider.api.PushServiceControllerProvider;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.settings.PassportUidProvider;
import io.appmetrica.analytics.push.settings.PushFilter;
import io.appmetrica.analytics.push.settings.PushFilteredCallback;
import io.appmetrica.analytics.push.settings.PushMessageTracker;
import io.appmetrica.analytics.push.settings.PushNotificationFactory;
import java.util.Map;

/**
 * Main entry point for AppMetrica Push SDK.
 */
public final class AppMetricaPush {

    private AppMetricaPush() {}

    /**
     * The {@link String} for extracting user defined {@code payload}.
     *
     * <p>If you send silent push you can extract payload from silent push {@link android.content.BroadcastReceiver}
     * {@link android.content.Intent}.</p>
     *
     * <p><b>EXAMPLE</b></p>
     * import io.appmetrica.analytics.push.AppMetricaPush;
     *
     * <pre class="code"><code class="java">
     *  public class SilentPushReceiver extends BroadcastReceiver {
     *
     *      {@literal @}Override
     *      public void onReceive(Context context, Intent intent) {
     *          //extract you payload
     *          String payload = intent.getStringExtra(AppMetricaPush.PAYLOAD);
     *          //process your payload here
     *      }
     *  }
     * }
     * </code></pre>
     *
     * <p>If you send general push you can extract payload from your deeplink {@link android.app.Activity} or from your
     * launch activity with {@link android.content.Intent#CATEGORY_DEFAULT} and
     * {@link android.content.Intent#ACTION_MAIN} if push message notification action url is not defined.</p>
     *
     * <p><b>EXAMPLE</b></p>
     * <pre class="code"><code class="java">
     *      import io.appmetrica.analytics.push.AppMetricaPush;
     *
     *      public class DeeplinkOrLaunchActivity extends Activity {
     *
     *          {@literal @}Override
     *          protected void onCreate(Bundle savedInstanceState) {
     *              super.onCreate(saveInstanceState);
     *              handlePayload(getIntent());
     *          }
     *
     *          {@literal @}Override
     *          protected void onNewIntent(Intent intent) {
     *              super.onNewIntent(intent);
     *              handlePayload(intent);
     *          }
     *
     *          private void handlePayload(Intent intent) {
     *              //extract you payload
     *              String payload = intent.getStringExtra(AppMetricaPush.PAYLOAD);
     *              //process your payload here
     *          }
     *      }
     * </code></pre>
     *
     * @see android.content.BroadcastReceiver
     * @see android.content.Intent
     * @see android.content.Intent#CATEGORY_DEFAULT
     * @see android.content.Intent#ACTION_MAIN
     * @see android.app.Activity
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    public static final String EXTRA_PAYLOAD = ".extra.payload";

    /**
     * Key for extra action info in {@link Intent}.
     */
    public static final String EXTRA_ACTION_INFO = "io.appmetrica.analytics.push.extra.ACTION_INFO";

    /**
     * <p>{@link android.content.Intent} action for performing opening
     * <a href = "https://developer.android.com/reference/android/content/Intent.html#CATEGORY_DEFAULT">default</a>
     * <a href = "https://developer.android.com/reference/android/content/Intent.html#CATEGORY_LAUNCHER">launcher</a>
     * activity. You can use it for detecting application launch on AppMetrica Push message notification action.
     * </p>
     * <p><b>EXAMPLE</b></p>
     * <pre class="code"><code class="java">
     *      import io.appmetrica.analytics.push.AppMetricaPush;
     *
     *      public class LaunchActivity extends Activity {
     *
     *          {@literal @}Override
     *          protected void onCreate(Bundle savedInstanceState) {
     *              super.onCreate(saveInstanceState);
     *              Intent intent = getIntent();
     *              String action = intent.getAction();
     *              if (AppMetricaPush.OPEN_DEFAULT_ACTIVITY_ACTION.equals(action)) {
     *                  //Process opening application via AppMetrica Push message notification action
     *              }
     *          }
     *
     *      }
     *
     * </code></pre>
     */
    public static final String OPEN_DEFAULT_ACTIVITY_ACTION = "io.appmetrica.analytics.push.action.OPEN";

    @SuppressLint("StaticFieldLeak")
    private static volatile AppMetricaPushCore implInstance;
    private static final Object LOCK = new Object();

    /**
     * Activates {@link AppMetricaPush} with {@link Context}.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before activation of {@link AppMetricaPush}.
     *
     * @param ctx {@link Context} object. Any application context.
     * @throws IllegalStateException if
     *                               <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     *                               AppMetrica SDK</a> has not been activated yet.
     * @see Context
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK documentation</a>
     */
    public static synchronized void activate(@NonNull final Context ctx) {
        if (implInstance == null) {
            synchronized (LOCK) {
                if (implInstance == null) {
                    AppMetricaPushCore appMetricaPushCore = AppMetricaPushCore.getInstance(ctx);
                    appMetricaPushCore.init();
                    implInstance = appMetricaPushCore;
                }
            }
        }
    }

    /**
     * Activates {@link AppMetricaPush} with {@link Context}.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before activation of {@link AppMetricaPush}.
     *
     * @param ctx {@link Context} object. Any application context
     * @param providers list of {@link PushServiceControllerProvider}
     * @throws IllegalStateException if
     *                               <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     *                               AppMetrica SDK</a> has not been activated yet.
     * @see Context
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK documentation</a>
     */
    public static synchronized void activate(@NonNull final Context ctx, PushServiceControllerProvider... providers) {
        if (implInstance == null) {
            synchronized (LOCK) {
                if (implInstance == null) {
                    AppMetricaPushCore appMetricaPushCore = AppMetricaPushCore.getInstance(ctx);
                    appMetricaPushCore.init(providers);
                    implInstance = appMetricaPushCore;
                }
            }
        }
    }

    /**
     * Method returns used push tokens. {@link AppMetricaPush} should be activated before.
     *
     * @return push tokens {@link Map<String, String>} or null if the tokens are not yet available
     * @throws IllegalStateException if {@link AppMetricaPush} has not been activated yet.
     * @see AppMetricaPush
     */
    @Nullable
    public static synchronized Map<String, String> getTokens() {
        synchronized (LOCK) {
            checkActivated();
        }
        TokenManager tokenManager = implInstance.getTokenManager();
        if (tokenManager == null) {
            return null;
        }
        return tokenManager.getPlainTokens();
    }

    /**
     * Sets {@link TokenUpdateListener}.
     *
     * @param listener {@link TokenUpdateListener} instance
     */
    public static synchronized void setTokenUpdateListener(@NonNull TokenUpdateListener listener) {
        synchronized (LOCK) {
            checkActivated();
        }
        implInstance.setTokenUpdateListener(listener);
    }

    private static void checkActivated() {
        if (implInstance == null) {
            throw new IllegalStateException(
                "AppMetricaPush should be activated by calling AppMetricaPush.activate(Context)."
            );
        }
    }

    /**
     * Method returns default notification channel. {@link AppMetricaPush} should be activated before.
     *
     * @return notification channel {@link NotificationChannel} or null if api level less 28
     * @throws IllegalStateException if {@link AppMetricaPush} has not been activated yet.
     * @see NotificationChannel
     */
    @Nullable
    public static synchronized NotificationChannel getDefaultNotificationChannel() {
        synchronized (LOCK) {
            checkActivated();
        }
        return implInstance.getDefaultNotificationChannel();
    }

    /**
     * Sets custom push notification factory.
     *
     * @param context {@link Context} to use
     * @param pushNotificationFactory custom {@link PushNotificationFactory}
     */
    public static void setPushNotificationFactory(@NonNull final Context context,
                                                  @NonNull final PushNotificationFactory pushNotificationFactory) {
        PushNotificationFactoryProvider.setPushNotificationFactory(pushNotificationFactory);
    }

    /**
     * @return default {@link PushNotificationFactory}
     */
    @NonNull
    public static PushNotificationFactory getDefaultPushNotificationFactory() {
        return PushNotificationFactoryProvider.getDefaultPushNotificationFactory();
    }

    /**
     * Sets auto tracking configuration.
     *
     * @param context {@link Context} to use
     * @param autoTrackingConfiguration custom {@link AutoTrackingConfiguration}
     */
    public static void setAutoTrackingConfiguration(
        @NonNull final Context context,
        @NonNull final AutoTrackingConfiguration autoTrackingConfiguration
    ) {
        AppMetricaPushCore.getInstance(context).getPushServiceProvider()
            .setAutoTrackingConfiguration(autoTrackingConfiguration);
    }

    /**
     * Adds custom push filter.
     *
     * @param context {@link Context} to use
     * @param pushFilter custom {@link PushFilter}
     */
    public static void addPushFilter(@NonNull Context context, @NonNull PushFilter pushFilter) {
        AppMetricaPushCore core = AppMetricaPushCore.getInstance(context);
        core.getPushFilterFacade().addPushFilter(pushFilter);
        core.getPreLazyFilterFacade().addPushFilter(pushFilter);
    }

    /**
     * Adds custom push filter callback.
     *
     * @param context {@link Context} to use
     * @param pushFilteredCallback custom {@link PushFilteredCallback}
     */
    public static void addPushFilteredCallback(@NonNull Context context,
                                               @NonNull PushFilteredCallback pushFilteredCallback) {
        AppMetricaPushCore core = AppMetricaPushCore.getInstance(context);
        core.getPushFilterFacade().addPushCallback(pushFilteredCallback);
        core.getPreLazyFilterFacade().addPushCallback(pushFilteredCallback);
    }

    /**
     * Sets custom passport UID provider.
     *
     * @param context {@link Context} to use
     * @param passportUidProvider custom {@link PassportUidProvider}
     */
    public static void setPassportUidProvider(@NonNull final Context context,
                                              @NonNull final PassportUidProvider passportUidProvider) {
        AppMetricaPushCore.getInstance(context).setPassportUidProvider(passportUidProvider);
    }

    /**
     * Sets custom location provider.
     *
     * @param locationProvider custom {@link LocationProvider}
     */
    public static void setLocationProvider(@NonNull final LocationProvider locationProvider) {
        LocationProviderHolder.setProvider(locationProvider);
    }

    /**
     * Adds custom push message tracker.
     *
     * @deprecated Use {@link AppMetricaPush#addPushEventListener(PushEventListener)} instead.
     * @param tracker custom {@link PushMessageTracker}
     */
    @Deprecated
    public static void addPushMessageTracker(@NonNull final PushMessageTracker tracker) {
        PushMessageTrackerHub.getInstance().registerTracker(new PushMessageTrackerWrapper(tracker));
    }

    /**
     * Enables more logs.
     */
    public static void enableLogger() {
        PublicLogger.INSTANCE.setEnabled(true);
    }

    /**
     * @return {@link NotificationCustomizersHolder} that can be used to set custom {@link NotificationValueProvider}
     */
    public static NotificationCustomizersHolder getNotificationCustomizersHolder() {
        return NotificationCustomizersHolderProvider.getCustomizersHolder();
    }

    /**
     * Sets custom lazy push transform rule provider.
     *
     * @param provider custom {@link LazyPushTransformRuleProvider}
     */
    public static void setLazyPushTransformRuleProvider(@NonNull final LazyPushTransformRuleProvider provider) {
        LazyPushTransformRuleProviderHolder.setProvider(provider);
    }

    /**
     * Adds a push event listener to handle push notification events.
     *
     * @param listener The {@link PushEventListener} implementation to handle push events
     * @see PushEventListener
     */
    public static void addPushEventListener(@NonNull final PushEventListener listener) {
        PushMessageTrackerHub.getInstance().registerTracker(new PushEventListenerWrapper(listener));
    }

    /**
     * Reports a custom push event to AppMetrica for tracking and analytics.
     *
     * <p>Use this method to manually report push events for your own push notifications that are not sent from AppMetrica.</p>
     * <p><a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK</a> should be activated before it, otherwise the event will not be sent.</p>
     *
     * <p>For create push events use static methods of the {@link PushEvent} class.</p>
     *
     * <p><b>EXAMPLE:</b>
     * <pre>{@code
     * // Create event
     * OpenPushEvent event = PushEvent.openEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param context   {@link Context} object. Any application context
     * @param pushEvent custom push event to report. Can be created using static methods of the {@link PushEvent} class.
     *                  Will be ignored if null
     */
    public static void reportPushEvent(
        @NonNull final Context context,
        @Nullable final PushEvent pushEvent
    ) {
        if (pushEvent != null) {
            AppMetricaPushCore.getInstance(context).reportPushEvent(pushEvent);
        }
    }
}
