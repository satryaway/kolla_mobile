package com.jixstreet.kolla.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.jixstreet.kolla.library.Callback;

/**
 * Requesting permissions
 *
 * @author fachrifebrian on 1/31/17.
 */
public class PermissionUtils {

    public static final String TAG = "PermissionUtil";
    public static final int REQUEST_STORAGE = 1;
    public static final int REQUEST_CAMERA = 2;
    public static final int REQUEST_CONTACT = 3;
    public static final int REQUEST_PHONE_STATE = 4;
    public static final int REQUEST_LOCATION = 5;

    public static final String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String[] PERMISSIONS_CAMERA = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static final String[] PERMISSIONS_CONTACTS = {Manifest.permission.READ_CONTACTS};

    public static final String[] PERMISSIONS_PHONE_STATE = {Manifest.permission.READ_PHONE_STATE};

    public static final String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION};

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermissions(@NonNull Activity activity,
                                           @NonNull String[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (String result : grantResults) {
            if (ActivityCompat.checkSelfPermission(activity, result)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean shouldShowRequestPermissions(@NonNull Activity activity,
                                                        @NonNull String[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (String result : grantResults) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, result)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check permission
     */
    public static void isPermissionsGranted(@NonNull Activity activity,
                                            @NonNull String[] permission,
                                            int requestCode,
                                            @NonNull String title) {
        if (!checkPermissions(activity, permission)) {
            Log.d(TAG, "not granted is " + !checkPermissions(activity, permission));
            requestPermissions(activity, permission, requestCode, title);
        } else {
            Log.d(TAG, "not granted is " + !checkPermissions(activity, permission));
        }
    }

    /**
     * request permission
     */
    public static void requestPermissions(@NonNull final Activity activity,
                                          @NonNull final String[] permission,
                                          final int requestCode,
                                          @NonNull String message) {
        if (shouldShowRequestPermissions(activity, permission)) {
            ActivityCompat.requestPermissions(activity, permission, requestCode);
        } else {
            DialogUtils.modalCancelable(activity, "Alert", message, false, new Callback<Boolean>() {
                @Override public void run(@Nullable Boolean param) {
                    ActivityCompat.requestPermissions(activity, permission, requestCode);
                }
            });
        }
    }
}
