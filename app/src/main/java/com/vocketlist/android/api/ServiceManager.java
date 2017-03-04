package com.vocketlist.android.api;

import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;
import com.vocketlist.android.api.baseservice.ScheduleService;
import com.vocketlist.android.api.errorchecker.ScheduleErrorChecker;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Schedule;
import com.vocketlist.android.network.converter.EnumParameterConverterFactory;
import com.vocketlist.android.network.converter.gson.GsonConverterFactory;
import com.vocketlist.android.network.error.handler.ErrorHandlingCallAdapterBuilder;
import com.vocketlist.android.network.error.handler.FirebaseErrorHandler;
import com.vocketlist.android.network.error.handler.RxErrorHandler;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.HttpResponseErrorInterceptor;
import com.vocketlist.android.network.service.LazyMockInterceptor;
import com.vocketlist.android.network.service.LoggingInterceptor;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;
import com.vocketlist.android.network.service.WebkitCookieJar;
import com.vocketlist.android.network.utils.Timeout;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-19.
 */

public class ServiceManager {
	public static final LazyMockInterceptor mockInterceptor = new LazyMockInterceptor();

	private static final String BASE_URL = AppApplication.getInstance().getString(R.string.voketBaseUrl);


	public final static OkHttpClient.Builder mDefaultHttpClientBuilder = new OkHttpClient.Builder()
			.cookieJar(new WebkitCookieJar())
			.connectTimeout(Timeout.getConnectionTimeout(), Timeout.UNIT)
			.readTimeout(Timeout.getReadTimeout(), Timeout.UNIT)
			.addInterceptor(new LoginInterceptor())
			.addInterceptor(new HttpResponseErrorInterceptor())
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

	public Observable<Response<BaseResponse<Schedule>>> getScheduleList(){

		return  retrofit.create(ScheduleService.class)
				.getScheduleList()
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<>(new ScheduleErrorChecker()));
	}
}

