package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ControllerListUtils {

    @NonNull
    public static ArrayList<BasePushServiceController> createControllerList(@NonNull Context context,
                                                                            @Nullable final String senderId) {
        Identifier identifier = new Identifier(senderId);
        IdentifierExtractor identifierExtractor = new MockableIdentifierExtractor(context, identifier);
        ArrayList<BasePushServiceController> controllerList = new ArrayList<>();
        controllerList.add(new BasePushServiceController(context, identifierExtractor));

        return controllerList;
    }

}
