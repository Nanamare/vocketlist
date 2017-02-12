package com.vocketlist.android;

import android.app.Application;

import com.vocketlist.android.network.utils.NetworkState;
import com.vocketlist.android.roboguice.log.Ln;

/**
 * Created by lsit on 2017. 1. 30..
 */
public class AppApplication extends Application {
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
    }

    public static AppApplication getInstance() {
        return instance;
    }
}
