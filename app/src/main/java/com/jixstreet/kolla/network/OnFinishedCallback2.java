package com.jixstreet.kolla.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author satryaway@gmail.com
 */
public interface OnFinishedCallback2<R, T> {
    void handle(@NonNull ResultType type,
                @Nullable R response,
                @Nullable T tag,
                @Nullable String errorMsg);
}