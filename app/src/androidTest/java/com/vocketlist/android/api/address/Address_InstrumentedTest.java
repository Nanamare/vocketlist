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
    private List<AddressInfo> mFirstAddressList;

    @Before
    public void setup() {
        mFirstAddressList = null;

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
        AddressServiceManager.getsFirstAddress()
                .subscribe(new EmptySubscriber<Response<BaseResponse<List<AddressInfo>>>>() {
                    @Override
                    public void onNext(Response<BaseResponse<List<AddressInfo>>> baseResponseResponse) {
                        mFirstAddressList = baseResponseResponse.body().mResult;
                    }
                });

        assertNotNull(mFirstAddressList);
        assertTrue(mFirstAddressList.size() > 0);
    }
}
