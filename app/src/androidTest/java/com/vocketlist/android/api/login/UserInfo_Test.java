package com.vocketlist.android.api.login;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.api.ServiceManager;
import com.vocketlist.android.api.SimpleSubscriber;
import com.vocketlist.android.dto.BaseResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Response;
import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */
@RunWith(AndroidJUnit4.class)
public class UserInfo_Test {
    private LoginModel mLoginModel;

    @Before
    public void setup() {
        mLoginModel = null;

        ServiceManager.mockInterceptor.setResponse(null);

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test
    public void 페이스북_로그인된_상황에서_vocketlist_서비스에_로그인_시도_Test() {
        // 본 테스트를 위해서는 Facebook에 로그인이 되어 있는 상황이어야 한다.

        UserServiceManager.autoLogin()
                .subscribe(new SimpleSubscriber<Response<BaseResponse<LoginModel>>>() {
                    @Override
                    public void onNext(Response<BaseResponse<LoginModel>> baseResponseResponse) {
                        super.onNext(baseResponseResponse);

                        mLoginModel = baseResponseResponse.body().mResult;
                    }
                });

        assertNotNull(mLoginModel);
    }
}
