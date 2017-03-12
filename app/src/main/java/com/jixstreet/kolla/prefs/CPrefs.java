package com.jixstreet.kolla.prefs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static final synchronized <T> void writeList(@NonNull Context ctx,
                                                        @NonNull String key,
                                                        @Nullable ArrayList<T> collection) {
        String value = new Gson().toJson(collection);
        write(ctx, key, value, String.class);
    }

    public static final synchronized <T> List<T> readList(@NonNull Context ctx,
                                                          @NonNull String key,
                                                          @NonNull Class<T[]> tClass) {
        String value = read(ctx, key, String.class);
        final T[] obj = new Gson().fromJson(value, tClass);
        return obj != null ? Arrays.asList(obj) : null;
    }
}
