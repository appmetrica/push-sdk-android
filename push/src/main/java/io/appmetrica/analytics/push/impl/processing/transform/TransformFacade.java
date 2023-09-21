package io.appmetrica.analytics.push.impl.processing.transform;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.model.PushMessage;

public class TransformFacade extends TransformProcessor {

    @NonNull
    private final TransformController transformController;

    public TransformFacade(@NonNull Context context) {
        this(context, new TransformController());
    }

    @VisibleForTesting
    TransformFacade(@NonNull Context context, @NonNull TransformController transformController) {
        this.transformController = transformController;
        addTransformProcessors(
            new LazyPushTransformProcessor(context),
            new FilterTransformProcessor(context)
        );
    }

    @NonNull
    @Override
    public TransformResult transform(@NonNull PushMessage pushMessage) {
        return transformController.transform(pushMessage);
    }

    public void addTransformProcessor(@NonNull TransformProcessor processor) {
        transformController.addTransformProcessor(processor);
    }

    private void addTransformProcessors(@NonNull TransformProcessor... processors) {
        for (TransformProcessor processor : processors) {
            addTransformProcessor(processor);
        }
    }
}
