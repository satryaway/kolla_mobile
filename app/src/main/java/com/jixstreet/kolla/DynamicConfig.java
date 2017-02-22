package com.jixstreet.kolla;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jixstreet.kolla.prefs.KPrefs;
import com.jixstreet.kolla.prefs.PrefKey;
import com.jixstreet.kolla.utility.StringUtils;

/**
 * App configuration that can be set/changed by user. <br />
 * First will check if specific setting exist on shared preference, if not yet exist then
 * default value from BuildConfig will be used instead.
 *
 * @author Dwi C on 12/7/2015.
 */
@SuppressWarnings("FinalStaticMethod")
public class DynamicConfig {

    public static final String getBaseUrl(@NonNull Context context) {
        String url = KPrefs.read(context, PrefKey.STR_BASE_URL, String.class);
        if (!TextUtils.isEmpty(url))
            return url;

        // temporary, until all project migrated to caps.payacces.co.id
        // TODO: remove this quick hack later.
        if (!BuildConfig.DEBUG)
            return BuildConfig.BASE_URL;
        else
            return BuildConfig.BASE_URL;
    }

    public static final int getBasePort(@NonNull Context context) {
        Integer port = KPrefs.read(context, PrefKey.INT_BASE_URL_PORT, Integer.class);
        if (port != null)
            return port; // auto unboxing
        else
            return BuildConfig.API_PORT;
    }

    /**
     * Used by image loader as it can't set port number beforehand.
     */
    public static final String getBaseUrlWithPort(@NonNull Context context) {
        return StringUtils.combineUrlWithPort(getBaseUrl(context), getBasePort(context));
    }
}
