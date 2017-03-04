package com.vocketlist.android.api.user;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.iid.FirebaseInstanceId;
import com.vocketlist.android.api.LoginInterceptor;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.service.EmptySubscriber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Response;
import rx.Scheduler;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */
@RunWith(AndroidJUnit4.class)
public class UserInfoService_Test {
    private BaseResponse<Void> mResponseRegisterFcm;
    private LoginModel mLoginModel;

    @Before
    public void setup() {
        mLoginModel = null;
        mResponseRegisterFcm = null;

        ServiceDefine.mockInterceptor.setResponse(null);

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test
    public void 비로그인_상태에서_FCM_토큰_등록_테스트() {
        String token = LoginInterceptor.getLoginToken();
        LoginInterceptor.setLoginToken(null);

        UserServiceManager.registerFcmToken(FirebaseInstanceId.getInstance().getToken())
                .subscribe(new Subscriber<Response<BaseResponse<Void>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
                        mResponseRegisterFcm = baseResponseResponse.body();
                    }
                });

        LoginInterceptor.setLoginToken(token);

        assertNotNull(mResponseRegisterFcm);
        assertTrue(mResponseRegisterFcm.mSuccess);
        assertNull(mResponseRegisterFcm.mResult);
    }

    @Test
    public void 페이스북_로그인된_상황에서_vocketlist_서비스에_로그인_시도_Test() {
        // 본 테스트를 위해서는 Facebook에 로그인이 되어 있는 상황이어야 한다.

        UserServiceManager.autoLogin()
                .subscribe(new EmptySubscriber<Response<BaseResponse<LoginModel>>>() {
                    @Override
                    public void onNext(Response<BaseResponse<LoginModel>> baseResponseResponse) {
                        super.onNext(baseResponseResponse);

                        mLoginModel = baseResponseResponse.body().mResult;
                    }
                });

        assertNotNull(mLoginModel);
    }

    @Test
    public void 서비스_로그인된_상태에서_FCM_토큰_등록_Test() {
        페이스북_로그인된_상황에서_vocketlist_서비스에_로그인_시도_Test();

        UserServiceManager.registerFcmToken(FirebaseInstanceId.getInstance().getToken())
                .subscribe(new Subscriber<Response<BaseResponse<Void>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
                        mResponseRegisterFcm = baseResponseResponse.body();
                    }
                });

        assertNotNull(mResponseRegisterFcm);
        assertTrue(mResponseRegisterFcm.mSuccess);
        assertNull(mResponseRegisterFcm.mResult);
    }
}
