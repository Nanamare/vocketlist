package com.sicamp.android.noti.common.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

final class PreferenceCache {
	private static HashMap<String, CashData> mCashData = new HashMap<String, CashData>();
	private static final long MAX_BUFFER_SIZE = 50;
	private static final long TIME_REMOVE = 60000;		// 오래된 데이터의 기준 시간.

	public static final int DATA_TYPE_BOOLEAN	= 1;
	public static final int DATA_TYPE_INTEGER	= 2;
	public static final int DATA_TYPE_LONG		= 3;
	public static final int DATA_TYPE_FLOAT		= 4;
	public static final int DATA_TYPE_STRING	= 5;
	
	public static void setPreference(Context context, String pref, String key, boolean value) {
		setPreference(context, pref, key, new Boolean(value), DATA_TYPE_BOOLEAN);
	}
	
	public static void setPreference(Context context, String pref, String key, int value) {
		setPreference(context, pref, key, new Integer(value), DATA_TYPE_INTEGER);
	}
	
	public static void setPreference(Context context, String pref, String key, long value) {
		setPreference(context, pref, key, new Long(value), DATA_TYPE_LONG);
	}
	
	public static void setPreference(Context context, String pref, String key, float value) {
		setPreference(context, pref, key, new Float(value), DATA_TYPE_FLOAT);
	}
	
	public static void setPreference(Context context, String pref, String key, String value) {
		setPreference(context, pref, key, value, DATA_TYPE_STRING);
	}
	
	/**
	 * boolean 형의 데이터를 가져온다.
	 * 
	 * @param pref
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getPreferenceBoolean(Context context, String pref, String key, boolean defaultValue) {
		Object value = getCashData(pref, key);
		
		if (value == null) {
			SharedPreferences sharedPref = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
			boolean data = sharedPref.getBoolean(key, defaultValue);
			
			putCashData(pref, key, data, DATA_TYPE_BOOLEAN);
			return data;
		}
		
		return (Boolean) value;
	}
	
	/**
	 * 
	 * 
	 * @param pref
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getPreferenceInt(Context context, String pref, String key, int defaultValue) {
		Object value = getCashData(pref, key);
		
		if (value == null) {
			SharedPreferences sharedPref = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
			int data = sharedPref.getInt(key, defaultValue);
			
			putCashData(pref, key, data, DATA_TYPE_INTEGER);
			return data;
		}
		
		return (Integer) value;
	}
	
	public static long getPreferenceLong(Context context, String pref, String key, long defaultValue) {
		Object value = getCashData(pref, key);
		
		if (value == null) {
			SharedPreferences sharedPref = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
			long data = sharedPref.getLong(key, defaultValue);
			
			putCashData(pref, key, data, DATA_TYPE_LONG);
			return data;
		}
		
		return (Long) value;
	}
	
	public static float getPreferenceFloat(Context context, String pref, String key, float defaultValue) {
		Object value = getCashData(pref, key);
		
		if (value == null) {
			SharedPreferences sharedPref = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
			float data = sharedPref.getFloat(key, defaultValue);
			
			putCashData(pref, key, data, DATA_TYPE_FLOAT);
			return data;
		}
		
		return (Float) value;
	}
	
	public static String getPreferenceString(Context context, String pref, String key, String defaultValue) {
		Object value = getCashData(pref, key);
		
		if (value == null) {
			SharedPreferences sharedPref = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
			String data = sharedPref.getString(key, defaultValue);
			
			putCashData(pref, key, data, DATA_TYPE_STRING);
			return data;
		}
		
		return (String) value;
	}
	
	/**
	 * Cash에 데이터를 저장하고, 비 동기적으로 데이터를 Preference에 저장한다.
	 * 
	 * @param context
	 * @param pref
	 * @param key
	 * @param value
	 * @param type
	 */
	private synchronized static void setPreference(Context context, String pref, String key, Object value, int type) {
		putCashData(pref, key, value, type);
		
		/** 비 동기적으로 데이터를 저장한다. */
		if (mPrefHandler == null) {
			createSaveHandler(context);
		}
		
		SavePreferenceData saveData = new SavePreferenceData();

		saveData.mPreference 	= pref;
		saveData.mKey			= key;
		saveData.mData 			= value;
		
		Message msg = mPrefHandler.obtainMessage();
		msg.obj 	= saveData;
		
		mPrefHandler.sendMessage(msg);
	}
	
	private static HandlerThread mThread;
	private static Looper mServiceLooper;
	private static SaveHandler 		mPrefHandler;
	
	private static final void createSaveHandler(Context context) {
		mThread = new HandlerThread("ServiceStartArgs", Process.THREAD_PRIORITY_BACKGROUND);
		mThread.start();
		
		mServiceLooper 	= mThread.getLooper();
		mPrefHandler 	= new SaveHandler(mServiceLooper, context);
	}
	
	/**
	 * 캐시에 저장된 데이터를 읽어온다. 
	 */
	private static Object getCashData(String pref, String key) {
		String hashKey = generateHashKey(pref, key);
		CashData data = mCashData.get(hashKey);
		
		if (data == null) {
			return null;
		}
		
		return data.mData;
	}
	
	/**
	 * 데이터를 캐시에 저장한다.
	 * 만일 캐시의 저장 공간이 꽉! 찾을때는 오래된 데이터를 삭제한 이후 저장한다. 
	 * 이미 저장된 데이터의 경우에는 데이터를 교체 처리한다.
	 * 
	 * @param pref
	 * @param key
	 * @param value
	 * @param type
	 */
	private synchronized static void putCashData(String pref, String key, Object value, int type) {
		String hashKey = generateHashKey(pref, key);
		CashData	newData	= new CashData(value, type);
		
		if (mCashData.containsKey(hashKey)) {
			// 사전에 사용된 데이터의 경우 교체한다.
			CashData data = mCashData.get(hashKey);
			data.setData(value, type);
			return;
		}
		
		if (mCashData.size() >= MAX_BUFFER_SIZE) {
			// 버퍼가 꽉! 찾을때에만 오래된 데이터를 제거한다. 
			removeOldestData();
		}
		mCashData.put(hashKey, newData);
	}
	
	/**
	 * 오래된 데이터 제거.
	 */
	private static void removeOldestData() {
		Set<Entry<String, CashData>> setEntry =  mCashData.entrySet();
		Iterator<Entry<String, CashData>> iterator = setEntry.iterator(); // = mCashData.values().iterator();
		String strKey	= null;
		CashData			data	= null;
		long				currentTime = System.currentTimeMillis();
		long 				oldestTime = currentTime;
		long				removeCnt = 0;
		
		while (iterator.hasNext()) {
			Entry<String, CashData> entry = iterator.next();
			
			data = entry.getValue();
			
			if (data.mSaveTime < oldestTime) {
				strKey 		= entry.getKey();
				oldestTime 	= data.mSaveTime;
			}
			
			// 오래된 데이터는 삭제한다.
			if ((currentTime - data.mSaveTime) > TIME_REMOVE) {
				iterator.remove();
				++removeCnt;
			}
		}
		
		if (removeCnt == 0 && strKey != null) {
			// 삭제된 데이터가 없는 경우 가장 오래된 데이터 1개를 삭제한다.
			mCashData.remove(strKey);
		}
	}
	
	/**
	 * CashData에서 데이터를 기록하고 찾기위한 Key를 얻는다.
	 * 생성되는 키는 "[PREF]/[KEY]"의 형식으로 생성된다.
	 * 
	 * 그러므로 pref, key에는 '[', ']', '/'가 와서는 안된다.
	 * 
	 * @param pref
	 * @param key
	 * @return
	 */
	private static String generateHashKey(String pref, String key) {
		StringBuilder hashKey = new StringBuilder();
		
		hashKey.append("[");
		hashKey.append(pref);
		hashKey.append("]/[");
		hashKey.append(key);
		hashKey.append("]");
		
		return hashKey.toString();
	}
	
	static class CashData {
		public Object mData;		// 저장될 데이터.
		public int 	mDataType;		// 데이터 타입.
		public long	mSaveTime;		// milliseconde time
		
		CashData(Object data, int dataType) {
			setData(data, dataType);
		}
		
		private void setData(Object data, int dataType) {
			mData = data;
			mSaveTime = System.currentTimeMillis();
		}
	}

	static class SavePreferenceData {
		public String mPreference;
		public String mKey;
		public Object mData;
		
		private SavePreferenceData()  { }
	}
	
	static class SaveHandler extends Handler {
		private Context mContext;
		
		SaveHandler(Looper looper, Context context) {
			super(looper);
			
			mContext = context;
		}

		@Override
		public void handleMessage(Message msg) {
			
			SavePreferenceData 			data 	= (SavePreferenceData) msg.obj;
			SharedPreferences pref 	= mContext.getSharedPreferences(data.mPreference, Context.MODE_PRIVATE);
			SharedPreferences.Editor	editor 	= pref.edit();
			
			if (data.mData instanceof Boolean) {
				editor.putBoolean(data.mKey, (Boolean) data.mData);
				
			} else if (data.mData instanceof Integer) {
				editor.putInt(data.mKey, (Integer) data.mData);
				
			} else if (data.mData instanceof Long) {
				editor.putLong(data.mKey, (Long) data.mData);
				
			} else if (data.mData instanceof Float) {
				editor.putFloat(data.mKey, (Float) data.mData);
				
			} else if (data.mData instanceof String) {
				editor.putString(data.mKey, (String) data.mData);
				
			} else {
				return;
			}

			editor.commit();
		}
	}
}
