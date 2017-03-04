package com.vocketlist.android.api.user;

import com.facebook.AccessToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vocketlist.android.api.LoginInterceptor;
import com.vocketlist.android.api.ServiceManager;
import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.common.helper.DeviceHelper;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;
import com.vocketlist.android.preference.FacebookPreperence;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public class UserServiceManager {
    private static UserService sUserService = ServiceManager.retrofit.create(UserService.class);

    public static Observable<Response<BaseResponse<Void>>> registerFcmToken(String fcmToken) {
        return sUserService
                .registerToken(fcmToken, DeviceHelper.getDeviceId())
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<BaseResponse<Void>>(new BaseServiceErrorChecker<Void>()));
    }

    public static Observable<Response<BaseResponse<LoginModel>>> autoLogin() {
        // 현재는 Facebook 전용이여서 Facebook을 이용한 로그인이되도록 처리되어있다.
        // todo : 차후 여러 로그인을 제공할 경우 그에 맞춰서 추가 로직이 필요하다

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String token = (accessToken != null) ? accessToken.getToken() : "";

        return loginWithFacebook(FacebookPreperence.getInstance().getUserInfo(),
                        token,
                        FacebookPreperence.getInstance().getUserId());
    }

    public static Observable<Response<BaseResponse<LoginModel>>> loginWithFacebook(String userInfo, String token, String userId) {
        return sUserService
                .loginFb(userInfo, token, userId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<LoginModel>()))
                .map(new Func1<Response<BaseResponse<LoginModel>>, Response<BaseResponse<LoginModel>>>() {
                    @Override
                    public Response<BaseResponse<LoginModel>> call(Response<BaseResponse<LoginModel>> responseBodyResponse) {
                        LoginInterceptor.setLoginToken(responseBodyResponse.body().mResult.mToken);

                        UserServiceManager
                                .registerFcmToken(FirebaseInstanceId.getInstance().getToken())
                                .subscribe(new EmptySubscriber<Response<BaseResponse<Void>>>());

                        return responseBodyResponse;
                    }
                });
    }
}
