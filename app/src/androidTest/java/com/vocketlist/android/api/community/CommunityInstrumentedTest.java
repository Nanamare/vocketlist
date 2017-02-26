package com.vocketlist.android.api.community;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.community.CommunityDetail;
import com.vocketlist.android.dto.community.CommunityList;
import com.vocketlist.android.net.CommunityServiceManager;
import com.vocketlist.android.roboguice.log.Ln;

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
import static junit.framework.Assert.assertTrue;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
@RunWith(AndroidJUnit4.class)
public class CommunityInstrumentedTest {
    private BaseResponse<CommunityList> mListResponse;
    private BaseResponse<CommunityDetail> mDetailResponse;

    @Before
    public void setup() {
        mListResponse = null;

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.immediate();
            }
        });
    }


    @Test
    public void 커뮤니티_정보_가져오기() {
        CommunityServiceManager.list(1)
                .subscribe(new Subscriber<Response<BaseResponse<CommunityList>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Ln.e(e, "onError");
                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommunityList>> baseResponseResponse) {
                        mListResponse = baseResponseResponse.body();
                    };

                });

        assertNotNull(mListResponse);
        assertTrue(mListResponse.mSuccess);
    }

    @Test
    public void 커뮤니티_상세_정보() {
        CommunityServiceManager.detail(4)
                .subscribe(new Subscriber<Response<BaseResponse<CommunityDetail>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommunityDetail>> baseResponseResponse) {
                        mDetailResponse = baseResponseResponse.body();
                    }
                });

        assertNotNull(mDetailResponse);
        assertTrue(mDetailResponse.mSuccess);
    }
}
