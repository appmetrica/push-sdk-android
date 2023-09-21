package io.appmetrica.analytics.push.provider.firebase.impl;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.FirebaseOptions;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;

public class Identifier {

    @Nullable
    private final String apiKey;
    @Nullable
    private final String appId;
    @Nullable
    private final String senderId;
    @Nullable
    private final String projectId;

    Identifier(@Nullable String apiKey,
               @Nullable String appId,
               @Nullable String senderId,
               @Nullable String projectId) {
        this.apiKey = apiKey;
        this.appId = appId;
        this.senderId = senderId;
        this.projectId = projectId;
    }

    @Nullable
    String getApiKey() {
        return apiKey;
    }

    @Nullable
    String getAppId() {
        return appId;
    }

    @Nullable
    String getSenderId() {
        return senderId;
    }

    @Nullable
    String getProjectId() {
        return projectId;
    }

    @NonNull
    FirebaseOptions toFirebaseOptions() {
        FirebaseOptions.Builder builderOptions = new FirebaseOptions.Builder();
        builderOptions.setApplicationId(appId);
        builderOptions.setGcmSenderId(senderId);
        if (CoreUtils.isNotEmpty(apiKey)) {
            builderOptions.setApiKey(apiKey);
        }
        if (CoreUtils.isNotEmpty(projectId)) {
            builderOptions.setProjectId(projectId);
        }

        return builderOptions.build();
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(appId) == true && TextUtils.isEmpty(senderId) == true;
    }

    public boolean isValid() {
        return TextUtils.isEmpty(appId) == false && TextUtils.isEmpty(senderId) == false;
    }
}
