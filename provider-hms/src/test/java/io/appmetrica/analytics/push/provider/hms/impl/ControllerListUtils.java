package io.appmetrica.analytics.push.provider.hms.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ControllerListUtils {

    @NonNull
    public static ArrayList<BasePushServiceController> createControllerList(
        @NonNull Context context,
        @Nullable final String appId
    ) {
        Identifier identifier = new Identifier(appId);
        IdentifierExtractor identifierExtractor = new MockableIdentifierExtractor(context, identifier);
        ArrayList<BasePushServiceController> controllerList = new ArrayList<BasePushServiceController>();
        controllerList.add(new BasePushServiceController(context, identifierExtractor));

        return controllerList;
    }

}
