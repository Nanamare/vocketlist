package com.vocketlist.android;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.vocketlist.android.api.user.LoginModel;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.network.utils.NetworkState;
import com.vocketlist.android.roboguice.log.Ln;

import retrofit2.Response;

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
        tryAutoLogin();
    }

    private void tryAutoLogin() {
        UserServiceManager.autoLogin()
                .subscribe(new EmptySubscriber<Response<BaseResponse<LoginModel>>>());
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
