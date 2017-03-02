package com.jixstreet.kolla.parent;

import android.content.Context;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

public abstract class ModelJson {
    public abstract String getRoute();
    public abstract void cancel();

    protected Context context;
    public String ROUTE;
    public String PrefKey;

    public ModelJson(Context context) {
        this.context = context;
        setAPI();
    }

    private void setAPI() {
        ROUTE = getRoute();
    }
}
