package com.vocketlist.android.api;

import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;
import com.vocketlist.android.api.baseservice.ScheduleService;
import com.vocketlist.android.api.baseservice.VoketService;
import com.vocketlist.android.api.errorchecker.ApplyVolunteerErrorChecker;
import com.vocketlist.android.api.errorchecker.ScheduleErrorChecker;
import com.vocketlist.android.api.errorchecker.VocketCategoryErrorChecker;
import com.vocketlist.android.api.errorchecker.VoketDetailErrorChecker;
import com.vocketlist.android.api.errorchecker.VoketErrorChecker;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Schedule;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.network.converter.EnumParameterConverterFactory;
import com.vocketlist.android.network.converter.gson.GsonConverterFactory;
import com.vocketlist.android.network.error.handler.ErrorHandlingCallAdapterBuilder;
import com.vocketlist.android.network.error.handler.RxErrorHandler;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.LazyMockInterceptor;
import com.vocketlist.android.network.service.LoggingInterceptor;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;
import com.vocketlist.android.network.service.WebkitCookieJar;
import com.vocketlist.android.network.utils.Timeout;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
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
			.addNetworkInterceptor(new LoggingInterceptor(AppApplication.getInstance().getContext()));


	public final static Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addCallAdapterFactory(ErrorHandlingCallAdapterBuilder.create()
					.setCallAdapterFactory(RxJavaCallAdapterFactory.create())
					.addErrorHandler(RxErrorHandler.class)
					.build())
			.addConverterFactory(EnumParameterConverterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.client(mDefaultHttpClientBuilder.build())
			.build();

	public Observable<Response<BaseResponse<Volunteer>>> getVoketList(int page) {
		return retrofit.create(VoketService.class)
				.getVoketList(page)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<BaseResponse<Volunteer>>(new VoketErrorChecker()));
	}

	public Observable<Response<BaseResponse<Volunteer>>> getVocketCategoryList(String category, int page) {
		return retrofit.create(VoketService.class)
				.getVocketCategoryList(category, page)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<>(new VocketCategoryErrorChecker()));

	}

	public Observable<Response<BaseResponse<VolunteerDetail>>> getVoketDetail(int voketId) {
		return retrofit.create(VoketService.class)
				.getVoketDetail(voketId)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<BaseResponse<VolunteerDetail>>(new VoketDetailErrorChecker()));
	}

	public Observable<Response<ResponseBody>> applyVolunteer(String name, String phone,int service_id){

		return retrofit.create(VoketService.class)
				.applyVolunteer(name, phone,service_id)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<>(new ApplyVolunteerErrorChecker()));

	}

	public Observable<Response<BaseResponse<Schedule>>> getScheduleList(){

		return  retrofit.create(ScheduleService.class)
				.getScheduleList()
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<>(new ScheduleErrorChecker()));
	}
}

