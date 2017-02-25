package com.vocketlist.android.net;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.vocketlist.android.util.SharePrefUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
//			.addInterceptor(new DefaultHeaderInterceptor())
//			.addInterceptor(new MockInterpolator())
//			.addInterceptor(mockInterceptor)
			.addNetworkInterceptor(new LoggingInterceptor())
			.addInterceptor(chain -> { //헤더에 토큰 추가
				Request original = chain.request();
				String token = SharePrefUtil.getSharedPreference("token");
				Request.Builder requestBuilder = original.newBuilder()
						.header("Authorization", "JWT " + token);
				Request request = requestBuilder.build();
				return chain.proceed(request);
			});



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
		retrofit.create(UserService.class)
				.registerToken(token)
				.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
				.lift(new ServiceErrorChecker<BaseResponse<Boolean>>(new FcmRegisterErrorChecker()))
				.doOnSubscribe(new Action0() {
					@Override
					public void call() {

					}
				})
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


	public Boolean getStatusResult(String json) {
		try {
			JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
			boolean result = ja.get("success").getAsBoolean();
			String message = ja.get("message").getAsString();
			if (result && message.equals("OK")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}

