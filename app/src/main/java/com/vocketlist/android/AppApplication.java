package com.vocketlist.android;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.vocketlist.android.network.utils.NetworkState;
import com.vocketlist.android.roboguice.log.Ln;

/**
 * Created by lsit on 2017. 1. 30..
 */
public class AppApplication extends MultiDexApplication {
    private static AppApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        this.instance = this;

        inits();
    }

    private void inits() {
        Ln.init(this);
        NetworkState.init(this);
        initFacebook();
    }

    private void initFacebook() {
        // facebook event log 활성
        FacebookSdk.sdkInitialize(this);
        registerActivityLifecycleCallbacks(new FacebookEventLogger());

        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        }
    }

    public Context getContext() {
        return this.getApplicationContext();
    }

    public static AppApplication getInstance() {
        return instance;
    }
}
