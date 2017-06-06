package com.jixstreet.kolla.network;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.Convert;
import com.jixstreet.kolla.utility.Log;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.Locale;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

@SuppressWarnings("FinalStaticMethod")
public class SysUtils {

    /**
     * Requirement is to use Indonesian language regardless of system locale.
     * This is required for caldroid and locale-related date format, otherwise english will be shown.
     */
    @SuppressWarnings("deprecation")
    public static final void setDefaultLocaleToIndonesia() {
        try {
            Locale locale = new Locale("in");
            Locale.setDefault(locale);

            // NOTE: You must NEVER modify a Configuration object that Android gives you.
            // Make a copy with config = new Configuration(config) first.
            // Also context.getResources() will not work, only global Resources.getSystem() works.

            Configuration config = new Configuration(Resources.getSystem().getConfiguration());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                config.setLocale(locale);
            else
                config.locale = locale;
            Resources.getSystem().updateConfiguration(config, null);

        } catch (Exception ex) {
            Log.printStackTrace(ex);
        }
    }

    /**
     * Return SHA1 hash of input bytes.
     */
    public static final byte[] getSHA1Hash(byte[] bytesInput) {
        byte[] bytesResult = new byte[0]; // empty byte array
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(bytesInput, 0, bytesInput.length);
            bytesResult = md.digest();
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        return bytesResult;
    }

    /**
     * Return SHA1 hash of input string.
     */
    public static final String getSHA1Hash(String strInput) {
        String strHash = null;
        try {
            byte[] bytesResult = getSHA1Hash(strInput.getBytes("UTF-8"));
            if (bytesResult.length > 0)
                strHash = Convert.bytesToBase16String(bytesResult);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        return strHash;
    }

    /**
     * Check if there's active internet connection. Need manifest permission.
     */
    public static final boolean isOnline(@NonNull Context context) {
        final ConnectivityManager conMgr = Convert.as(ConnectivityManager.class,
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        if (conMgr == null)
            return false;

        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());
    }

    /**
     * Return true if all String fields of specific object is not empty.
     */
    public static final boolean validateAllStringNotEmpty(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getType() == String.class) {
                Object o = null;
                try {
                    o = f.get(obj);
                } catch (Exception e) {
                    Log.printStackTrace(e);
                }
                String s = Convert.as(String.class, o);
                if (TextUtils.isEmpty(s))
                    return false;
            }
        }
        return true;
    }

    /**
     * Try to hide soft keyboard UI. Won't have effect if called from activity
     * onCreate(), you have to use android manifest android:windowSoftInputMode="stateHidden"
     * for that.
     */
    public static final void hideSoftKeyboard(@NonNull Context context, @NonNull View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ex) {
            Log.printStackTrace(ex);
        }
    }

    /**
     * Force an activity to always have portrait or reverse portrait orientation, but only when
     * device's "natural" orientation is portrait. If natural orientation is landscape, any
     * rotation is allowed. <br />
     * <br />
     * Natural orientation are either portrait or landscape, never reverse portrait or reverse
     * landscape.
     */
    @SuppressWarnings("deprecation")
    public static final void lockPortraitNaturalOrientation(@NonNull Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int height;
        int width;

        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;

        switch (rotation) {
            case Surface.ROTATION_90:
                // portrait to landscape, force back to portrait
                if (width > height)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

            case Surface.ROTATION_180:
                // portrait to reverse portrait, ok
                if (height > width)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;

            case Surface.ROTATION_270:
                // portrait to reverse landscape, force back to portrait
                if (width > height)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

            case Surface.ROTATION_0:
                // portrait, ok
                if (height > width)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * Create status bar notification and and show it using NotificationManager.
     *
     * @param id ID is required to display multiple notification from same context.
     * @param pi PendingIntent to execute when notification clicked.
     */
    public static final void postStatusBarNotification(@NonNull Context context,
                                                       int id,
                                                       @Nullable String line1,
                                                       @Nullable String line2,
                                                       @Nullable PendingIntent pi) {
        Notification notification = createStatusBarNotification(context, line1, line2, pi);
        notification.defaults = Notification.DEFAULT_SOUND;

        NotificationManager nmgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nmgr != null)
            nmgr.notify(id, notification);
    }

    /**
     * Create standard notification tailored for this app use (icons, text, etc).
     * NOTE: By default sound is not included.
     */
    @NonNull
    public static final Notification createStatusBarNotification(@NonNull Context context,
                                                                 @Nullable String line1,
                                                                 @Nullable String line2,
                                                                 @Nullable PendingIntent pi) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setSmallIcon(R.drawable.ic_stat_notification);
//        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(line1);
        builder.setContentText(line2);
        builder.setAutoCancel(true); // dismiss notification when clicked
        builder.setTicker(line1 + " " + line2); // also display in status bar
        builder.setContentIntent(pi);

        // big style text will effect on Android 4.1 (API 16) and above.
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(line2));

        return builder.build();
    }

}
