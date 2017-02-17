package com.jixstreet.kolla.utility;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by satryaway on 2/16/2017.
 * satryaway@gmail.com
 */

public class ViewUtils {

    public static LinearLayoutManager getLayoutManager(Context context, final boolean isVertical) {
        return new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return isVertical;
            }
        };
    }

    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
