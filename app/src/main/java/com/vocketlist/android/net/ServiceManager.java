package com.vocketlist.android.net;

import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;
import com.vocketlist.android.net.baseservice.UserService;
import com.vocketlist.android.net.errorchecker.JsonError;
import com.vocketlist.android.net.errorchecker.JsonErrorChecker;
import com.vocketlist.android.network.converter.EnumParameterConverterFactory;
import com.vocketlist.android.network.converter.gson.GsonConverterFactory;
import com.vocketlist.android.network.error.handler.ErrorHandlingCallAdapterBuilder;
import com.vocketlist.android.network.error.handler.RxErrorHandler;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.LoggingInterceptor;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;
import com.vocketlist.android.network.service.WebkitCookieJar;
import com.vocketlist.android.network.utils.Timeout;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by kinamare on 2017-02-19.
 */

public class ServiceManager {

	private static final String BASE_URL = AppApplication.getInstance().getString(R.string.voketBaseUrl);

	private static OkHttpClient.Builder mDefaultHttpClientBuilder = new OkHttpClient.Builder()
			.cookieJar(new WebkitCookieJar())
			.connectTimeout(Timeout.getConnectionTimeout(), Timeout.UNIT)
			.readTimeout(Timeout.getReadTimeout(), Timeout.UNIT)
//			.addInterceptor(new DefaultHeaderInterceptor())
//			.addInterceptor(new MockInterpolator())
			.addNetworkInterceptor(new LoggingInterceptor());

	private static Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addCallAdapterFactory(ErrorHandlingCallAdapterBuilder.create()
					.setCallAdapterFactory(RxJavaCallAdapterFactory.create())
					.addErrorHandler(RxErrorHandler.class)
					.build())
			.addConverterFactory(EnumParameterConverterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.client(mDefaultHttpClientBuilder.build())
			.build();

	public void registerFcmToken(String token){
		retrofit.create(UserService.class)
				.registerToken(token)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<>(new JsonErrorChecker()))
				.doOnSubscribe(new Action0() {
					@Override
					public void call() {

					}
				})
				.subscribe(new Subscriber<Response<String>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Response<String> stringResponse) {

					}
				});



	}


}

