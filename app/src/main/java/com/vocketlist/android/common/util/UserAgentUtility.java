package com.vocketlist.android.common.util;

import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;

public final class UserAgentUtility {
	public static String sUserAgent = null;
    public static String sUserAgentForWebView = null;

	public static String appName = AppApplication.getInstance().getString(R.string.app_name);
	
	public static String getUserAgentString() {
		if (sUserAgent == null) {
			StringBuilder userAgent = new StringBuilder(128);
			userAgent.append(String.format("%s/", appName));
			userAgent.append(Version.getCurrentVersionName(AppApplication.getInstance()));
			userAgent.append("(Android OS ");
			userAgent.append(android.os.Build.VERSION.RELEASE).append(";");
			userAgent.append(android.os.Build.MANUFACTURER).append(" ");
			userAgent.append(android.os.Build.MODEL).append(")");
			
			sUserAgent = getValidString(userAgent.toString());
		}

		return sUserAgent;
	}

    public static String getUserAgentForWebView(String defaultUserAgent) {
        if (sUserAgentForWebView != null) {
            return sUserAgentForWebView;
        }

        StringBuilder userAgent = new StringBuilder(128);
        userAgent.append(defaultUserAgent);
		userAgent.append(String.format(" %s/", appName));
        userAgent.append(Version.getCurrentVersionName(AppApplication.getInstance()));

        sUserAgentForWebView = getValidString(userAgent.toString());
        return sUserAgentForWebView;
    }

	// okhttp 사용시 이슈 대응을 위한 코드
	private static String getValidString(String value) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0, length = value.length(); i < length; i++) {
			char c = value.charAt(i);
			if (c <= '\u001f' || c >= '\u007f') {
				sb.append(String.format("\\u%04x", (int) c));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}
}
