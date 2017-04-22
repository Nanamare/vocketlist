package com.vocketlist.android.api.user;

import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.api.LoginInterceptor;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.common.helper.DeviceHelper;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;
import com.vocketlist.android.preference.FacebookPreperence;
import com.vocketlist.android.roboguice.log.Ln;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public final class UserServiceManager {
    private static final UserService SERVICE = ServiceDefine.retrofit.create(UserService.class);

    public static Observable<Response<BaseResponse<Void>>> registerFcmToken(String fcmToken) {
        return SERVICE
                .registerToken(fcmToken, DeviceHelper.getDeviceId())
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<BaseResponse<Void>>(new BaseServiceErrorChecker<Void>()));
    }

    private UserServiceManager() {

    }

    public static Observable<Response<BaseResponse<LoginModel>>> autoLogin() {
        // 현재는 Facebook 전용이여서 Facebook을 이용한 로그인이되도록 처리되어있다.
        // todo : 차후 여러 로그인을 제공할 경우 그에 맞춰서 추가 로직이 필요하다

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String token = (accessToken != null) ? accessToken.getToken() : "";

        Ln.d("facebook : token = " + token);
        return loginWithFacebook(FacebookPreperence.getInstance().getUserInfo(),
                        token,
                        FacebookPreperence.getInstance().getUserId());
    }

    public static Observable<Response<BaseResponse<LoginModel>>> loginWithFacebook(String userInfo, String token, String userId) {
        return SERVICE
                .loginFb(userInfo, token, userId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<LoginModel>()))
                .map(new Func1<Response<BaseResponse<LoginModel>>, Response<BaseResponse<LoginModel>>>() {
                    @Override
                    public Response<BaseResponse<LoginModel>> call(Response<BaseResponse<LoginModel>> responseBodyResponse) {
                        LoginInterceptor.setLoginToken(responseBodyResponse.body().mResult.mToken);
                        // 정상적으로 로그인이되었으면 서버에 토큰 정보를 전달한다.
                        // todo : 토큰 정보 전달시 실패되는 경우에 대하여 고려가 필요하다.
                        Ln.d("login token : " + responseBodyResponse.body().mResult.mToken);
                        UserServiceManager
                                .registerFcmToken(FirebaseInstanceId.getInstance().getToken())
                                .subscribe(new EmptySubscriber<Response<BaseResponse<Void>>>());

                        return responseBodyResponse;
                    }
                });
    }

    public static boolean isLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null
                && accessToken.isExpired() == false
                && !TextUtils.isEmpty(LoginInterceptor.getLoginToken());
    }

    public static void logout() {
        LoginManager.getInstance().logOut();
        LoginInterceptor.setLoginToken(null);
    }

    public static Observable<Response<BaseResponse<FavoritListModel>>> getFavorite() {
        return SERVICE.getFavorite()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<FavoritListModel>()));
    }

    public static Observable<Response<BaseResponse<FavoritListModel>>> setFavorite(List<String> favoriteList) {
        return SERVICE.setFavorite(favoriteList)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<FavoritListModel>()));
    }
}
