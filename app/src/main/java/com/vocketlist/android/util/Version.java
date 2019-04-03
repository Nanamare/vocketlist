package com.vocketlist.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;

public final class Version {

	public static String getCurrentVersionName(Context context) {
		if (context == null) {
			return "";
		}

		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int getCurrentVersionCode(Context context) {
		if (context == null) {
			return 0;
		}

		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
