package com.vocketlist.android.api.vocket;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.BaseResponse;

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
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */
@RunWith(AndroidJUnit4.class)
public class VocketService_Test {
    private BaseResponse<Volunteer> mVolunteer;
    private BaseResponse<VolunteerDetail> mDetail;
    private BaseResponse<Participate> mParticipate;

    @Before
    public void setup() {
        mVolunteer = null;
        mDetail = null;
        mParticipate = null;

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
    public void vocket_카테고리_별_리스트_가져오기_Test() {
        final int pageNum = 1;
        VocketServiceManager.getVocketList(Category.Activity, pageNum)
                .subscribe(new Subscriber<Response<BaseResponse<Volunteer>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<Volunteer>> baseResponseResponse) {
                        mVolunteer = baseResponseResponse.body();
                    }
                });

        assertNotNull(mVolunteer);
        assertTrue(mVolunteer.mSuccess);
        assertNotNull(mVolunteer.mResult);
        assertTrue(mVolunteer.mResult.mCount > 0);
        assertTrue(mVolunteer.mResult.mPageCurrent > 0);
        assertTrue(mVolunteer.mResult.mPageCurrent == pageNum);
        assertNotNull(mVolunteer.mResult.mDataList);
        assertTrue(mVolunteer.mResult.mDataList.size() > 0);

        for (Volunteer.Data data : mVolunteer.mResult.mDataList) {
            assertTrue(data.mId > 0);
            assertTrue(data.mOrganizationId > 0);
            assertNotNull(data.mTitle);
            assertNotNull(data.mImageUrl);
        }
    }

    @Test
    public void 보킷리스트_상세_정보보기_Test() {
        vocket_카테고리_별_리스트_가져오기_Test();

        VocketServiceManager.getVocketDetail(mVolunteer.mResult.mDataList.get(0).mId)
                .subscribe(new Subscriber<Response<BaseResponse<VolunteerDetail>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<VolunteerDetail>> baseResponseResponse) {
                        mDetail = baseResponseResponse.body();
                    }
                });

        assertNotNull(mDetail);
        assertTrue(mDetail.mSuccess);
        assertNotNull(mDetail.mResult);
        assertNotNull(mDetail.mResult.mTitle);
        assertNotNull(mDetail.mResult.mContent);
        assertNotNull(mDetail.mResult.mHostName);
        assertNotNull(mDetail.mResult.mFirstCategory);
        assertNotNull(mDetail.mResult.mImageUrl);
    }

    @Test
    public void 보킷리스트_봉사_신청_또는_취소_Test() {
        vocket_카테고리_별_리스트_가져오기_Test();

        VocketServiceManager.applyVolunteer(mVolunteer.mResult.mDataList.get(0).mId, "임승택", "010-3839-3981")
                .subscribe(new Subscriber<Response<BaseResponse<Participate>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<Participate>> participate) {
                        mParticipate = participate.body();
                    }
                });

        assertNotNull(mParticipate);
    }
}
