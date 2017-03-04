package com.vocketlist.android.common.helper;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.kakao.util.helper.log.Logger;
import com.vocketlist.android.AppApplication;
import com.vocketlist.android.preference.CommonPreference;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DeviceHelper {
    private static final String TAG = DeviceHelper.class.getSimpleName();

    private static final String TEMP_DEVICE_ID_TEXT = "00000000FF:FF:FF:FF:FF:FF";

    public static String getDeviceId() {
        String deviceId = CommonPreference.getInstance().getDeviceId();

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getHashedDeviceId(AppApplication.getInstance());
            CommonPreference.getInstance().setDeviceId(deviceId);
        }

        return deviceId;
    }

    private static String getHashedDeviceId(Context context) {
        // this is very first case, so make id as new one
        String deviceId = null;
        String hashedDeviceId = null;

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (!TextUtils.isEmpty(androidId)) {
            deviceId = androidId;
        } else {
            deviceId = TEMP_DEVICE_ID_TEXT;
        }

        try {
            Logger.d(TAG, "device id = " + deviceId);
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(deviceId.getBytes());
            byte[] hash = sha256.digest();
            hashedDeviceId = (new BigInteger(hash)).toString(16);
        } catch (NoSuchAlgorithmException e) {
            Logger.w(TAG, "getHashedDeviceId() failed." + e.getMessage());
            e.printStackTrace();
        }

        return hashedDeviceId;
    }
}
