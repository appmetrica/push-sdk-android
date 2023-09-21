package io.appmetrica.analytics.push.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PushServiceControllerComposite {

    @NonNull
    private final Context context;
    @NonNull
    private final Collection<PushServiceController> controllers;
    @NonNull
    private final Collection<String> titles;

    PushServiceControllerComposite(@NonNull Context context, @NonNull Collection<PushServiceController> controllers) {
        this.context = context;
        this.controllers = Collections.unmodifiableList(new ArrayList<PushServiceController>(controllers));
        ArrayList<String> tmpTitles = new ArrayList<String>(controllers.size());
        for (PushServiceController controller : controllers) {
            tmpTitles.add(controller.getTitle());
        }
        titles = Collections.unmodifiableList(tmpTitles);
    }

    public void register() {
        boolean wasRegistered = false;
        for (PushServiceController controller : controllers) {
            wasRegistered |= controller.register();
        }
        if (wasRegistered) {
            PushServiceFacade.initToken(context);
        }
    }

    @NonNull
    public Map<String, String> getTokens() {
        HashMap<String, String> tokens = new HashMap<String, String>();
        for (PushServiceController controller : controllers) {
            String title = controller.getTitle();
            String token = controller.getToken();
            tokens.put(title, token);
        }
        return Collections.unmodifiableMap(tokens);
    }

    @NonNull
    public Collection<String> getTitles() {
        return titles;
    }

}
