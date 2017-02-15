package com.jixstreet.kolla.utility;

import com.jixstreet.kolla.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class is made so we can have debug log independent of proguard settings.
 *
 * @author Dwi C on 12/20/2015.
 */
public class Log {
    private static final boolean LOG = BuildConfig.showDebugLog;

    public static void i(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
    }

    public static void d(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (LOG) android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (LOG) android.util.Log.w(tag, string);
    }

    public static void printStackTrace(Exception e) {
        if (LOG) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            android.util.Log.e("Log", sw.toString());
        }
    }

    // TODO: always show error regardless of debug or release build ?
    public static void e(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
    }

    public static void e(String tag, String string, Throwable throwable) {
        if (LOG) android.util.Log.e(tag, string, throwable);
    }

}