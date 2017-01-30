package com.sicamp.android.noti;

import android.app.Application;

import com.sicamp.android.network.utils.NetworkState;

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
        NetworkState.init(this);
    }

    public static AppApplication getInstance() {
        return instance;
    }
}
