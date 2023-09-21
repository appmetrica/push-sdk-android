package io.appmetrica.analytics.push.impl.processing.transform;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.model.PushMessage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransformController extends TransformProcessor {

    @NonNull
    private final List<TransformProcessor> transformProcessorList;

    public TransformController() {
        this.transformProcessorList = new CopyOnWriteArrayList<TransformProcessor>();
    }

    @NonNull
    @Override
    public TransformResult transform(@NonNull PushMessage pushMessage) {
        PushMessage cur = pushMessage;
        for (TransformProcessor transformProcessor : transformProcessorList) {
            try {
                TransformResult result = transformProcessor.transform(cur);
                if (result.isSuccess()) {
                    cur = result.pushMessage;
                } else {
                    return result;
                }
            } catch (TransformFailureException e) {
                return failure(cur, e.getCategory(), e.getDetails());
            }
        }
        return success(cur);
    }

    public void addTransformProcessor(@NonNull TransformProcessor processor) {
        transformProcessorList.add(processor);
    }
}
