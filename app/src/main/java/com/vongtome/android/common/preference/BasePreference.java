package com.vongtome.android.common.preference;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class BasePreference {
	private String mPrefKey;
	protected final Context mContext;

	/**
	 * SharedPreferences.Editor 객체
	 */
	public BasePreference(Context context, String prefKey) {
		mContext = context;
		mPrefKey = prefKey;
	}

	/**
	 * 환경 설정 저장.
	 * 
	 * @param key
	 *            저장될 키값.
	 * @param value
	 *            저장될 값.
	 */
	public void setPreference(String key, int value) {
		PreferenceCache.setPreference(mContext, mPrefKey, key, value);
	}

	public void setPreference(String key, long value) {
		PreferenceCache.setPreference(mContext, mPrefKey, key, value);
	}

	public void setPreference(String key, float value) {
		PreferenceCache.setPreference(mContext, mPrefKey, key, value);
	}

	public void setPreference(String key, boolean value) {
		PreferenceCache.setPreference(mContext, mPrefKey, key, value);
	}

	public void setPreference(String key, String value) {
		PreferenceCache.setPreference(mContext, mPrefKey, key, value);
	}

	/**
	 * 환경 설정 저장.
	 * 
	 * @param keys
	 *            저장될 키값들.
	 * @param values
	 *            저장될 값들.
	 */
	public void setPreference(String[] keys, Object[] values) {
		SharedPreferences pref = mContext.getSharedPreferences(mPrefKey, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = pref.edit();

		for (int i = 0; keys.length > i; i++) {
			if (values[i] instanceof String) {
				setPreference(keys[i], (String) values[i]);
			} else if (values[i] instanceof Integer) {
				setPreference(keys[i], (Integer) values[i]);
			} else if (values[i] instanceof Boolean) {
				setPreference(keys[i], (Boolean) values[i]);
			} else if (values[i] instanceof Long) {
				setPreference(keys[i], (Long) values[i]);
			} else if (values[i] instanceof Float) {
				setPreference(keys[i], (Float) values[i]);
			}
		}
	}

	/**
	 * 환경 설정파일에 저장된 값중에서 key에 해당되는
	 * 값을 문자열로 넘겨준다.
	 * 
	 * @param key
	 *            값을 가져올 키값.
	 * @return
	 */
	public String getPreferenceString(String key, String defaultValue) {
		return PreferenceCache.getPreferenceString(mContext, mPrefKey, key, defaultValue);
	}

	public int getPreferenceInt(String key, int defaultValue) {
		return PreferenceCache.getPreferenceInt(mContext, mPrefKey, key, defaultValue);
	}

	public boolean getPreferenceBoolean(String key, boolean defaultValue) {
		return PreferenceCache.getPreferenceBoolean(mContext, mPrefKey, key, defaultValue);
	}

	public long getPreferenceLong(String key, long defaultValue) {
		return PreferenceCache.getPreferenceLong(mContext, mPrefKey, key, defaultValue);
	}

	public float getPreferenceFloat(String key, float defaultValue) {
		return PreferenceCache.getPreferenceFloat(mContext, mPrefKey, key, defaultValue);
	}
}
