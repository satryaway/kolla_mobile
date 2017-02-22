package com.jixstreet.kolla.utility;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;

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
}