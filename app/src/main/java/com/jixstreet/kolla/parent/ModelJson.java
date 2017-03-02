package com.jixstreet.kolla.parent;

import android.content.Context;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

public abstract class ModelJson {
    public abstract String getRoute();
    public abstract Class getChildClass();
    public abstract void cancel();

    protected Context context;
    public static transient String ROUTE;
    public static transient String PrefKey;

    public ModelJson(Context context) {
        this.context = context;
        setAPI();
        setPrefKey();
    }

    private void setAPI() {
        ROUTE = getRoute();
    }

    private void setPrefKey() {
        PrefKey = getChildClass().getName().concat("1");
    }
}
