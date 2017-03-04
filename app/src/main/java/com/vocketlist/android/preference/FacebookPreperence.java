package com.vocketlist.android.preference;

import android.content.Context;

import com.vocketlist.android.AppApplication;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public class FacebookPreperence extends BasePreference {
    private static final String KEY_EMAIL = "key_facebook_email";
    private static final String KEY_USER_IMAGE_URL = "key_facebook_user_image_url";
    private static final String KEY_USER_NAME = "key_facebook_user_name";
    private static final String KEY_USER_INFO = "key_user_info";
    private static final String KEY_USER_ID = "key_user_id";

    private static FacebookPreperence instance;

    private FacebookPreperence(Context context, String prefKey) {
        super(context, prefKey);
    }

    public static FacebookPreperence getInstance() {
        if (FacebookPreperence.instance != null) {
            return FacebookPreperence.instance;
        }

        synchronized (FacebookPreperence.class) {
            if (FacebookPreperence.instance != null) {
                return FacebookPreperence.instance;
            }

            FacebookPreperence.instance = new FacebookPreperence(AppApplication.getInstance(), "FacebookInfo");
        }

        return FacebookPreperence.instance;
    }

    public void setEmail(String email) {
        setPreference(KEY_EMAIL, email);
    }

    public void setUserImageUrl(String userImageUrl) {
        setPreference(KEY_USER_IMAGE_URL, userImageUrl);
    }

    public String getUserImageUrl() {
        return getPreferenceString(KEY_USER_IMAGE_URL, null);
    }

    public void setUserName(String userName) {
        setPreference(KEY_USER_NAME, userName);
    }

    public String getUserName() {
        return getPreferenceString(KEY_USER_NAME, null);
    }

    public void setUserInfo(String userInfo) {
        setPreference(KEY_USER_INFO, userInfo);
    }

    public String getUserInfo() {
        return getPreferenceString(KEY_USER_INFO, null);
    }

    public void setUserId(String userId) {
        setPreference(KEY_USER_ID, userId);
    }

    public String getUserId() {
        return getPreferenceString(KEY_USER_ID, null);
    }
}
