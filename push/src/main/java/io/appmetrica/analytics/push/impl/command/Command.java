package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

public interface Command {

    void execute(@NonNull final Context context,
                 @NonNull final Bundle bundle);
}
