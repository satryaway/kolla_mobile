package com.jixstreet.kolla;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by satryaway on 2/28/2017.
 * satryaway@gmail.com
 */

public class KollaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        AppEventsLogger.activateApp(this);
        LeakCanary.install(this);
    }
}
