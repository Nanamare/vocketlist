package com.vocketlist.android.api;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SeungTaek.Lim on 2017. 2. 25..
 */
public class LoginInterceptor implements Interceptor {
    private volatile static String mLoginToken;

    public static synchronized void setLoginToken(String token) {
        mLoginToken = token;
    }

    public static String getLoginToken() {
        if (mLoginToken == null) {
            return mLoginToken;
        }

        return new String(mLoginToken);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();

        if (TextUtils.isEmpty(mLoginToken) == false) {
                requestBuilder.header("authorization", "JWT " + mLoginToken);
        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
