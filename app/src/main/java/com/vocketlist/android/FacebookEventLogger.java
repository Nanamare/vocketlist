package com.vocketlist.android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.facebook.appevents.AppEventsLogger;

/**
 * 참고 : https://developers.facebook.com/docs/app-events/android/
 * Created by SeungTaek.Lim on 2017. 2. 8..
 */
public class FacebookEventLogger implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        AppEventsLogger.activateApp(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        AppEventsLogger.deactivateApp(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
