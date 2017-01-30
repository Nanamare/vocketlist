package com.sicamp.android.network.service;

import com.sicamp.android.roboguice.util.Ln;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by SeungTaek.Lim on 2016. 11. 30..
 */

class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Ln.d(message);
    }
}
