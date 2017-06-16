package com.jixstreet.kolla.utility;

import com.crashlytics.android.Crashlytics;

/**
 * Created by satryaway on 6/16/2017.
 * satryaway@gmail.com
 */

public class CrashUtils {
    public static void logUser(String id, String email, String name) {
        Crashlytics.setUserIdentifier(id);
        Crashlytics.setUserEmail(email);
        Crashlytics.setUserName(name);
    }
}
