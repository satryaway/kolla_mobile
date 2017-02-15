package com.jixstreet.kolla.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.jixstreet.kolla.utility.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Cached shared preferences. Cached objects are stored in memory. Methods are
 * "synchronized", should be multithread-safe. <br />
 * <br />
 * Currently only support simple Object, String, Boolean, Integer, and Uri. <br />
 * <br />
 * NOTE: Generics will never accept primitives. int.class is the same as Integer.TYPE.
 *
 * @author Dwi C.
 */
public class CachedPreferences {
    private final SharedPreferences pref;
    private final HashMap<String, Object> map;

    public CachedPreferences(@NonNull Context ctx, @NonNull String filename) {
        pref = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        map = new HashMap<>();
    }

    @Nullable
    public final synchronized <T> T read(@NonNull String key, @NonNull Class<T> classt) {
        // read from cache
        Object o = map.get(key);

        // if not on cache, try to get from sharedpref
        if (o == null && pref.contains(key)) {
            if (classt == String.class)
                o = pref.getString(key, null);
            else if (classt == Boolean.class)
                o = pref.getBoolean(key, false);
            else if (classt == Integer.class)
                o = pref.getInt(key, 0);
            else if (classt == Uri.class) {
                String s = pref.getString(key, null);
                o = (s != null ? Uri.parse(s) : null);
            }

            // use this only on simple class, like POJO
            else {
                String s = pref.getString(key, null);
                if (s != null)
                    try {
                        o = new Gson().fromJson(s, classt);
                    } catch (Exception e) {
                        Log.printStackTrace(e);
                        o = null;
                    }
                else
                    o = null;
            }

            if (o != null)
                map.put(key, o);
        }
        return classt.cast(o);
    }

    public final synchronized <T> void write(@NonNull String key, @Nullable T value, @NonNull Class<T> classt) {
        // put on cache first, then put on sharedpref
        map.put(key, value);

        Editor editor = pref.edit();

        // NOTE: any cast to a null object will not throw exception, but unboxing a null could throw one.
        if (classt == String.class)
            editor.putString(key, (String) value);
        else if (classt == Boolean.class)
            editor.putBoolean(key, (value != null ? (Boolean) value : false));
        else if (classt == Integer.class)
            editor.putInt(key, (value != null ? (Integer) value : 0));
        else if (classt == Uri.class)
            editor.putString(key, (value != null ? value.toString() : null));
        else
            // use this only on simple class, like POJO
            editor.putString(key, (value != null ? new Gson().toJson(value) : null));

        editor.apply();
    }

    /**
     * Clear all data on both sharedpreferences and cache. Normally called when
     * logout.
     */
    public final synchronized void clear() {
        map.clear();

        Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Return map in memory
     */
    public final Map<String, Object> getMap() {
        return Collections.unmodifiableMap(map);
    }

}
