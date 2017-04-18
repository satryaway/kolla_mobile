package com.jixstreet.kolla.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.library.Callback;

/**
 * Created by satryaway on 2/22/2017.
 * satryaway@gmail.com
 */

public class DialogUtils {
    public static void makeSnackBar(String statusType, Context context, View view, String text) {
        int color = ContextCompat.getColor(context, R.color.color_failure);
        if (statusType.equals(CommonConstant.success))
            color = ContextCompat.getColor(context, R.color.color_success);

        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(color);
        sb.show();
    }

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void makeSnackBar(String statusType, Activity activity, String text) {
        if (activity == null) return;
        int color = ContextCompat.getColor(activity, R.color.color_failure);
        if (statusType.equals(CommonConstant.success))
            color = ContextCompat.getColor(activity, R.color.color_success);

        Snackbar sb = Snackbar.make(ViewUtils.getRootView(activity), text, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(color);
        sb.show();
    }

    public static void makeTwoButtonDialog(Context context, String title, String message,
                                           String okTitle, String cancelTitle,
                                           final Callback<Boolean> onCallback) {
        AlertDialog alert = new AlertDialog.Builder(context)
                .setTitle(title).setMessage(message).create();
        final DialogHelper helper = new DialogHelper();
        helper.callback = onCallback;
        alert.setButton(DialogInterface.BUTTON_POSITIVE, okTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.execute(true);
            }
        });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, cancelTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.execute(false);
            }
        });
        alert.show();
    }

    private static class DialogHelper {
        public Callback<Boolean> callback;
        private boolean executed = false;

        public void execute(boolean param) {
            if (!executed && callback != null)
                callback.run(param);
            executed = true;
        }
    }
}