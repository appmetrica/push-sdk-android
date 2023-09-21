package io.appmetrica.analytics.push.coreutils.internal.service;

import android.os.Bundle;
import androidx.annotation.NonNull;

public interface PushServiceCommandLauncher {

    void launchService(@NonNull Bundle extras);
}
