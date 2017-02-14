package com.vocketlist.android.message;

import android.content.Context;

import com.vocketlist.android.AppApplication;
import com.vocketlist.android.preference.BasePreference;

/**
 * Created by SeungTaek.Lim on 2017. 2. 2..
 */

public class FCMPreference extends BasePreference {
    private static final String KEY_TOKEN = "KEY_TOKEN";

    private static FCMPreference instance;

    private FCMPreference(Context context, String prefKey) {
        super(context, prefKey);
    }

    public static FCMPreference getInstance() {
        if (FCMPreference.instance != null) {
            return FCMPreference.instance;
        }

        synchronized (FCMPreference.class) {
            if (FCMPreference.instance != null) {
                return FCMPreference.instance;
            }

            FCMPreference.instance = new FCMPreference(AppApplication.getInstance(), "FCM");
        }

        return FCMPreference.instance;
    }

    public void saveToken(String token) {
        setPreference(KEY_TOKEN, token);
    }

    public String getToken() {
        return getPreferenceString(KEY_TOKEN, null);
    }
}
