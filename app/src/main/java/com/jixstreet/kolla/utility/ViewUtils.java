package com.jixstreet.kolla.utility;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

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
}
