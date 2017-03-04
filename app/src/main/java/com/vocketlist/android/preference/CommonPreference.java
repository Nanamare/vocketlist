package com.vocketlist.android.preference;

import android.content.Context;

import com.vocketlist.android.AppApplication;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public class CommonPreference extends BasePreference {
    private static final String KEY_DEVICE_ID = "key_device_id";
    private static CommonPreference instance;

    private CommonPreference(Context context, String prefKey) {
        super(context, prefKey);
    }

    public static CommonPreference getInstance() {
        if (CommonPreference.instance != null) {
            return CommonPreference.instance;
        }

        synchronized (CommonPreference.class) {
            if (CommonPreference.instance != null) {
                return CommonPreference.instance;
            }

            CommonPreference.instance = new CommonPreference(AppApplication.getInstance(), "COMMON");
        }

        return CommonPreference.instance;
    }

    public void setDeviceId(String deviceId) {
        setPreference(KEY_DEVICE_ID, deviceId);
    }

    public String getDeviceId() {
        return getPreferenceString(KEY_DEVICE_ID, null);
    }


}
