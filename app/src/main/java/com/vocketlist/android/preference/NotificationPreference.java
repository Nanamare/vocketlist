package com.vocketlist.android.preference;

import android.content.Context;

import com.vocketlist.android.AppApplication;

/**
 * Created by SeungTaek.Lim on 2017. 2. 25..
 */

public class NotificationPreference extends BasePreference {
    public static final String PREF_KEY = "Notification";
    private static final String KEY_NOTI_SETTING = "key_noti_setting";
    private static final String KEY_NOTI_RECOMMEND = "key_noti_recommend";
    private static final String KEY_NOTI_COMMUNITY = "key_noti_community";
    private static final String KEY_NOTI_NEW_VOLUNTEER = "key_noti_new_volunteer";

    private static NotificationPreference instance;

    private NotificationPreference(Context context, String prefKey) {
        super(context, prefKey);
    }

    public static NotificationPreference getInstance() {
        if (NotificationPreference.instance != null) {
            return NotificationPreference.instance;
        }

        synchronized (NotificationPreference.class) {
            if (NotificationPreference.instance != null) {
                return NotificationPreference.instance;
            }

            NotificationPreference.instance = new NotificationPreference(AppApplication.getInstance(), PREF_KEY);
        }

        return NotificationPreference.instance;
    }

    public void setNotiSetting(boolean use) {
        setPreference(KEY_NOTI_SETTING, use);
    }

    public boolean isNotiSetting() {
        return getPreferenceBoolean(KEY_NOTI_SETTING, false);
    }

    public void setRecommend(boolean use) {
        setPreference(KEY_NOTI_RECOMMEND, use);
    }

    public boolean isRecommend() {
        return getPreferenceBoolean(KEY_NOTI_RECOMMEND, false);
    }

    public void setNewVolunteer(boolean use) {
        setPreference(KEY_NOTI_NEW_VOLUNTEER, use);
    }

    public boolean isNewVolunteer() {
        return getPreferenceBoolean(KEY_NOTI_NEW_VOLUNTEER, false);
    }

    public void setCommunity(boolean use) {
        setPreference(KEY_NOTI_COMMUNITY, use);
    }

    public boolean isCommunity() {
        return getPreferenceBoolean(KEY_NOTI_COMMUNITY, false);
    }
}
