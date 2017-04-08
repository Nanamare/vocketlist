package com.vocketlist.android.api;

import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;
import com.vocketlist.android.network.converter.EnumParameterConverterFactory;
import com.vocketlist.android.network.converter.gson.GsonConverterFactory;
import com.vocketlist.android.network.error.handler.ErrorHandlingCallAdapterBuilder;
import com.vocketlist.android.network.error.handler.FirebaseErrorHandler;
import com.vocketlist.android.network.error.handler.RxErrorHandler;
import com.vocketlist.android.network.service.HttpResponseErrorInterceptor;
import com.vocketlist.android.network.service.LazyMockInterceptor;
import com.vocketlist.android.network.service.LoggingInterceptor;
import com.vocketlist.android.network.service.WebkitCookieJar;
import com.vocketlist.android.network.utils.Timeout;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by kinamare on 2017-02-19.
 */

public final class ServiceDefine {
	public static final LazyMockInterceptor mockInterceptor = new LazyMockInterceptor();

	private static final String BASE_URL = AppApplication.getInstance().getString(R.string.vocketBaseUrl);

	private ServiceDefine() {

	}

	public final static OkHttpClient.Builder mDefaultHttpClientBuilder = new OkHttpClient.Builder()
			.cookieJar(new WebkitCookieJar())
			.connectTimeout(Timeout.getConnectionTimeout(), Timeout.UNIT)
			.readTimeout(Timeout.getReadTimeout(), Timeout.UNIT)
			.addInterceptor(new LoginInterceptor())
			.addInterceptor(new HttpResponseErrorInterceptor())
			.addInterceptor(mockInterceptor)
			.addNetworkInterceptor(new LoggingInterceptor(AppApplication.getInstance().getContext()));


	public final static Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addCallAdapterFactory(ErrorHandlingCallAdapterBuilder.create()
					.setCallAdapterFactory(RxJavaCallAdapterFactory.create())
					.addErrorHandler(RxErrorHandler.class)
					.addErrorHandler(FirebaseErrorHandler.class)
					.build())
			.addConverterFactory(EnumParameterConverterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.client(mDefaultHttpClientBuilder.build())
			.build();
}

