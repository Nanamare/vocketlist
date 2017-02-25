package com.vocketlist.android.preference;

import android.content.Context;

import com.vocketlist.android.AppApplication;

/**
 * 공지사항
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 26.
 */
public class NoticePreference extends BasePreference {
    public static final String PREF_KEY = "Notice";
    private static final String KEY_TODAY = "key_today";

    private static NoticePreference instance;

    private NoticePreference(Context context, String prefKey) {
        super(context, prefKey);
    }

    public static NoticePreference getInstance() {
        if (NoticePreference.instance != null) {
            return NoticePreference.instance;
        }

        synchronized (FCMPreference.class) {
            if (NoticePreference.instance != null) {
                return NoticePreference.instance;
            }

            NoticePreference.instance = new NoticePreference(AppApplication.getInstance(), PREF_KEY);
        }

        return NoticePreference.instance;
    }

    public void setToday(boolean use) {
        setPreference(KEY_TODAY, use);
    }

    public boolean isToday() {
        return getPreferenceBoolean(KEY_TODAY, false);
    }
}
