package com.jixstreet.kolla.prefs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Static interface to clearable {@link CachedPreferences}.
 */
@SuppressWarnings("FinalStaticMethod")
public class CPrefs {

    private static final String FILE_NAME = "cprefs";
    private static CachedPreferences cp;

    @NonNull
    private static CachedPreferences instance(@NonNull Context ctx) {
        if (cp == null) {
            cp = new CachedPreferences(ctx, FILE_NAME);
        }
        return cp;
    }

    @Nullable
    public static final synchronized <T> T read(@NonNull Context ctx,
                                                @NonNull String key,
                                                @NonNull Class<T> classt) {
        return instance(ctx).read(key, classt);
    }

    public static final synchronized <T> void write(@NonNull Context ctx,
                                                    @NonNull String key,
                                                    @Nullable T value,
                                                    @NonNull Class<T> classt) {
        instance(ctx).write(key, value, classt);
    }

    public static final synchronized void clear(@NonNull Context ctx) {
        instance(ctx).clear();
    }

}
