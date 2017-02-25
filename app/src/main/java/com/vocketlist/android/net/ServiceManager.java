package com.vocketlist.android.net;

import android.os.Build;

import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Post;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.net.baseservice.CommunityService;
import com.vocketlist.android.net.baseservice.UserService;
import com.vocketlist.android.net.baseservice.VoketService;
import com.vocketlist.android.net.errorchecker.CommunityErrorChecker;
import com.vocketlist.android.net.errorchecker.FcmRegisterErrorChecker;
import com.vocketlist.android.net.errorchecker.LoginFbErrorChecker;
import com.vocketlist.android.net.errorchecker.VoketDetailErrorChecker;
import com.vocketlist.android.net.errorchecker.VoketErrorChecker;
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
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by kinamare on 2017-02-19.
 */

public class ServiceManager {
	public static final LazyMockInterceptor mockInterceptor = new LazyMockInterceptor();

	private static final String BASE_URL = AppApplication.getInstance().getString(R.string.voketBaseUrl);


	private static OkHttpClient.Builder mDefaultHttpClientBuilder = new OkHttpClient.Builder()
			.cookieJar(new WebkitCookieJar())
			.connectTimeout(Timeout.getConnectionTimeout(), Timeout.UNIT)
			.readTimeout(Timeout.getReadTimeout(), Timeout.UNIT)
			.addInterceptor(new DefaultHeaderInterceptor())
			.addNetworkInterceptor(new LoggingInterceptor(AppApplication.getInstance().getContext()));


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

	public void registerFcmToken(String token) {

		String device_id = getDeviceSerialNumber();
		retrofit.create(UserService.class)
				.registerToken(token,device_id)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<BaseResponse<Boolean>>(new FcmRegisterErrorChecker()))
				.subscribe(new Subscriber<Response<BaseResponse<Boolean>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(Response<BaseResponse<Boolean>> baseResponseResponse) {

					}
				});

	}

	public Observable<Response<ResponseBody>> loginFb(String userInfo, String token, String userId) {

		return retrofit.create(UserService.class)
				.loginFb(userInfo, token, userId)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<>(new LoginFbErrorChecker()))
				.doOnSubscribe(new Action0() {
					@Override
					public void call() {

					}
				});
	}

	public Observable<Response<BaseResponse<Volunteer>>> getVoketList(int page) {
		return retrofit.create(VoketService.class)
				.getVoketList(page)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<BaseResponse<Volunteer>>(new VoketErrorChecker()));
	}

	public Observable<Response<BaseResponse<VolunteerDetail>>> getVoketDetail(int voketId) {
		return retrofit.create(VoketService.class)
				.getVoketDetail(voketId)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<BaseResponse<VolunteerDetail>>(new VoketDetailErrorChecker()));
	}

	public Observable<Response<Post>> getCommunityList() {
		return retrofit.create(CommunityService.class)
				.getCommunityList()
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<Post>(new CommunityErrorChecker()))
				.doOnSubscribe(new Action0() {
					@Override
					public void call() {

					}
				});
	}


	private static String getDeviceSerialNumber() {
		try {
			return (String) Build.class.getField("SERIAL").get(null);
		} catch (Exception ignored) {
			return null;
		}
	}


}

