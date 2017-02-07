package com.vongtome.android.logger.network.service;

import com.vongtome.android.roboguice.log.Ln;

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
