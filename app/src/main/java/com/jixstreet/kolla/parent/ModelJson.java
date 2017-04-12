package com.jixstreet.kolla.parent;

import android.content.Context;

/**
 * Created by satryaway on 3/2/2017.
 * satryaway@gmail.com
 */

public abstract class ModelJson {
    private String id;

    public abstract String getRoute();
    public abstract void cancel();

    protected Context context;
    public String ROUTE;
    public String PrefKey;

    public ModelJson(Context context) {
        this.context = context;
        setAPI();
    }

    public ModelJson(Context context, String id) {
        this.context = context;
        this.id = id;
        setAPIWithId();
    }

    private void setAPIWithId() {
        ROUTE = getRoute() + id;
    }

    private void setAPI() {
        ROUTE = getRoute();
    }
}
