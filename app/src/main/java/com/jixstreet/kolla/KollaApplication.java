package com.jixstreet.kolla;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.leakcanary.LeakCanary;
import io.fabric.sdk.android.Fabric;

/**
 * Created by satryaway on 2/28/2017.
 * satryaway@gmail.com
 */

public class KollaApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        context = this;
        AppEventsLogger.activateApp(this);
        LeakCanary.install(this);
    }

    public static Context getContext() {
        return context;
    }
}
