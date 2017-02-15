package com.jixstreet.kolla.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Generic finished callback normally used as network response.
 *
 * @param <R> Type of object that will be sent to handle() method as 'response'
 *            parameter. For example: Object, Item[], JsonObject, JsonElement,
 *            LoginJson.Response, etc.
 * @param <T> Type of tag object.
 * @author Dwi C.
 */
public interface OnFinishedCallback2<R, T> {
    /**
     * @param response should not null when ResultType=Success
     * @param errorMsg should not null when ResultType=Fail
     */
    public void handle(@NonNull ResultType type,
                       @Nullable R response,
                       @Nullable T tag,
                       @Nullable String errorMsg);
}