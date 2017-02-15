package com.jixstreet.kolla.model;

import android.content.Context;
import android.util.Pair;


import com.jixstreet.kolla.BuildConfig;
import com.jixstreet.kolla.network.RDefault;
import com.jixstreet.kolla.prefs.CPrefs;

import java.util.ArrayList;

/**
 *
 */
public class LoginJson {

    public static final transient String API = "/api/user/login";

    private static final transient String PrefKey = LoginJson.class.getName().concat("1");

    public static class Request {
        public String app_code = "AREMA";
        public String platform = BuildConfig.PLATFORM_NAME;
        public String version = BuildConfig.VERSION_NAME;
        public String phone = "081222900985";
        public String passcode = "000000";

        public ArrayList<Pair<String, String>> createParams() {
            ArrayList<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("app_code", app_code));
            params.add(new Pair<>("platform", platform));
            params.add(new Pair<>("version", version));
            params.add(new Pair<>("phone", phone));
            params.add(new Pair<>("passcode", passcode));
            return params;
        }

        // REMINDER: never persist passcode, security concern
    }

    public static class Response extends RDefault {
        public String access_token;

        public static void saveAccessToken(Context ctx, Response resp) {
            CPrefs.write(ctx, PrefKey, resp.access_token, String.class);
        }

        public static void clearAccessToken(Context ctx) {
            CPrefs.write(ctx, PrefKey, null, String.class);
        }

        public static String getAccessToken(Context ctx) {
            return CPrefs.read(ctx, PrefKey, String.class);
        }
    }

}
