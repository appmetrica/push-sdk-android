package io.appmetrica.analytics.push.notification;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.impl.notification.NotificationBuilderMethodInvoker;
import io.appmetrica.analytics.push.model.PushMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Function;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import org.jetbrains.annotations.NotNull;

/**
 * Allows to set custom {@link NotificationCustomizer} while building {@link NotificationCompat}.
 * While building {@link NotificationCompat} Push SDK calls {@link NotificationCustomizer} in the following order.
 * <ol>
 * <li> beforeCustomizer
 * <li> all {@link NotificationCustomizer} from methods {@link NotificationCustomizersHolder#useProviderFor}
 * <li> afterCustomizer
 * </ol>
 * Also adds {@link Bundle} from {@link NotificationCustomizersHolder#useExtraBundleProvider} to
 * {@link PendingIntent} of all actions.
 */
public class NotificationCustomizersHolder {

    @NonNull
    private final Map<Function, NotificationCustomizer> customizers = new HashMap<>();
    @Nullable
    private ExtraBundleProvider extraBundleProvider;
    @Nullable
    private NotificationCustomizer beforeCustomizer;
    @Nullable
    private NotificationCustomizer afterCustomizer;

    /**
     * Sets {@link NotificationValueProvider} for {@link NotificationCompat.Builder} method
     * that should be called with 1 parameter.
     * For example {@link NotificationCompat.Builder#setSmallIcon(int)}.
     *
     * @param method link to method. For example {@code NotificationCompat.Builder::setSmallIcon}
     * @param provider provider that returns list of objects
     * @return the same {@link NotificationCustomizersHolder} object
     * @param <T> type
     */
    @NotNull
    public final <T> NotificationCustomizersHolder useProviderFor(
        @NotNull final Function2<NotificationCompat.Builder, T, NotificationCompat.Builder> method,
        @NotNull final NotificationValueProvider<T> provider
    ) {
        customizers.put(method, new NotificationCustomizer() {
            @Override
            public void invoke(
                @NonNull final NotificationCompat.Builder builder,
                @NonNull final PushMessage pushMessage
            ) {
                NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage);
            }
        });
        return this;
    }

    /**
     * Sets {@link NotificationValueProvider} for {@link NotificationCompat.Builder} method
     * that should be called with 2 parameters of one type.
     * For example {@link NotificationCompat.Builder#setSmallIcon(int, int)}.
     *
     * @param method link to method. For example {@code NotificationCompat.Builder::setSmallIcon}
     * @param provider provider that returns list of objects
     * @return the same {@link NotificationCustomizersHolder} object
     * @param <T> type
     */
    @NotNull
    public final <T> NotificationCustomizersHolder useProviderFor(
        @NotNull final Function3<NotificationCompat.Builder, T, T, NotificationCompat.Builder> method,
        @NotNull final NotificationValueProvider<List<T>> provider
    ) {
        customizers.put(method, new NotificationCustomizer() {
            @Override
            public void invoke(
                @NonNull final NotificationCompat.Builder builder,
                @NonNull final PushMessage pushMessage
            ) {
                NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage);
            }
        });
        return this;
    }

    /**
     * Sets {@link NotificationValueProvider} for {@link NotificationCompat.Builder} method
     * that should be called with 3 parameters of one type.
     * For example {@link NotificationCompat.Builder#setLights(int, int, int)}.
     *
     * @param method link to method. For example {@code NotificationCompat.Builder::setLights}
     * @param provider provider that returns list of objects
     * @return the same {@link NotificationCustomizersHolder} object
     * @param <T> type
     */
    @NotNull
    public final <T> NotificationCustomizersHolder useProviderFor(
        @NotNull final Function4<NotificationCompat.Builder, T, T, T, NotificationCompat.Builder> method,
        @NotNull final NotificationValueProvider<List<T>> provider
    ) {
        customizers.put(method, new NotificationCustomizer() {
            @Override
            public void invoke(
                @NonNull final NotificationCompat.Builder builder,
                @NonNull final PushMessage pushMessage
            ) {
                NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage);
            }
        });
        return this;
    }

    /**
     * Sets {@link NotificationValueProvider} for {@link NotificationCompat.Builder} method
     * that should be called several times.
     * For example {@link NotificationCompat.Builder#addAction(NotificationCompat.Action)}.
     *
     * @param method link to method. For example {@code NotificationCompat.Builder::addAction}
     * @param provider provider that returns list of objects
     * @return the same {@link NotificationCustomizersHolder} object
     * @param <T> type
     */
    @NotNull
    public final <T> NotificationCustomizersHolder useListProviderFor(
        @NotNull final Function2<NotificationCompat.Builder, T, NotificationCompat.Builder> method,
        @NotNull final NotificationValueProvider<List<T>> provider
    ) {
        customizers.put(method, new NotificationCustomizer() {
            @Override
            public void invoke(
                @NonNull final NotificationCompat.Builder builder,
                @NonNull final PushMessage pushMessage
            ) {
                NotificationBuilderMethodInvoker.invokeWithList(method, provider, builder, pushMessage);
            }
        });
        return this;
    }

    /**
     * @return map with all set {@link NotificationValueProvider} objects
     */
    @NotNull
    public final Map<Function, NotificationCustomizer> getCustomizers() {
        return new HashMap<>(customizers);
    }

    /**
     * Allows setting extra bundle to {@link Intent} that will be sent for actions.
     *
     * @param extraBundleProvider function that accepts {@link PushMessage} and gives {@link Bundle}
     * @return the same {@link NotificationCustomizersHolder} object
     */
    @NotNull
    public final NotificationCustomizersHolder useExtraBundleProvider(
        @NonNull final ExtraBundleProvider extraBundleProvider
    ) {
        this.extraBundleProvider = extraBundleProvider;
        return this;
    }

    /**
     * Getter for internal usage.
     *
     * @return extra bundle provider if it is set or null otherwise.
     */
    @Nullable
    public ExtraBundleProvider getExtraBundleProvider() {
        return extraBundleProvider;
    }

    /**
     * Sets customizer that is applied before all default {@link NotificationCustomizer}.
     *
     * @param beforeCustomizer custom {@link NotificationCustomizer}
     * @return the same {@link NotificationCustomizersHolder} object
     */
    @NonNull
    public NotificationCustomizersHolder useBeforeCustomizer(
        @NonNull final NotificationCustomizer beforeCustomizer
    ) {
        this.beforeCustomizer = beforeCustomizer;
        return this;
    }

    /**
     * Getter for internal usage.
     *
     * @return beforeCustomizer
     */
    @Nullable
    public NotificationCustomizer getBeforeCustomizer() {
        return beforeCustomizer;
    }

    /**
     * Sets customizer that is applied after all default {@link NotificationCustomizer}.
     *
     * @param afterCustomizer custom {@link NotificationCustomizer}
     * @return the same {@link NotificationCustomizersHolder} object
     */
    @NonNull
    public NotificationCustomizersHolder useAfterCustomizer(
        @NonNull final NotificationCustomizer afterCustomizer
    ) {
        this.afterCustomizer = afterCustomizer;
        return this;
    }

    /**
     * Getter for internal usage.
     *
     * @return afterCustomizer
     */
    @Nullable
    public NotificationCustomizer getAfterCustomizer() {
        return afterCustomizer;
    }
}
