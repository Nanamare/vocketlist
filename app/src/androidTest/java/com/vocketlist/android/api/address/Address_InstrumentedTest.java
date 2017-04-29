package com.vocketlist.android.api.address;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.service.EmptySubscriber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import retrofit2.Response;
import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by SeungTaek.Lim on 2017. 4. 29..
 */
@RunWith(AndroidJUnit4.class)
public class Address_InstrumentedTest {
    private List<AddressFirstInfo> mFirstAddressList;
    private List<AddressSecondInfo> mSecondAddressList;

    @Before
    public void setup() {
        mFirstAddressList = null;
        mSecondAddressList = null;

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
    public void 주소가져오기_테스트() {
        AddressServiceManager.getFirstAddress()
                .subscribe(new EmptySubscriber<Response<BaseResponse<List<AddressFirstInfo>>>>() {
                    @Override
                    public void onNext(Response<BaseResponse<List<AddressFirstInfo>>> baseResponseResponse) {
                        mFirstAddressList = baseResponseResponse.body().mResult;
                    }
                });

        assertNotNull(mFirstAddressList);
        assertTrue(mFirstAddressList.size() > 0);
    }

    @Test
    public void 두번째_주소_가져오기_테스트() {
        주소가져오기_테스트();

        AddressServiceManager.getSecondAddress(mFirstAddressList.get(0).mId)
                .subscribe(new EmptySubscriber<Response<BaseResponse<List<AddressSecondInfo>>>>() {
                    @Override
                    public void onNext(Response<BaseResponse<List<AddressSecondInfo>>> baseResponseResponse) {
                        mSecondAddressList = baseResponseResponse.body().mResult;
                    }
                });

        assertNotNull(mSecondAddressList);
        assertTrue(mSecondAddressList.size() > 0);
    }
}
