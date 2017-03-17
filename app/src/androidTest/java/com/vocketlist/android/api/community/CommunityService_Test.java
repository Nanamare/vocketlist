package com.vocketlist.android.api.community;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.community.model.CommunityDetail;
import com.vocketlist.android.api.community.model.CommunityList;
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
public class CommunityService_Test {
    private BaseResponse<CommunityList> mListResponse;
    private BaseResponse<CommunityDetail> mDetailResponse;

    @Before
    public void setup() {
        mListResponse = null;
        mDetailResponse = null;

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
        assertNotNull(mListResponse.mResult);
        assertTrue(mListResponse.mResult.mData.size() > 0);
        assertTrue(mListResponse.mResult.mPageNumber == 1);

        for (CommunityList.CommunityData communityData : mListResponse.mResult.mData) {
            assertNotNull(communityData.mAuthor);
            assertNotNull(communityData.mContent);
            assertNotNull(communityData.mCreateDate);
            assertNotNull(communityData.mImageUrl);
            assertNotNull(communityData.mUpdateDate);

            assertTrue(communityData.mAuthor.mId >= 0);
            assertNotNull(communityData.mAuthor.mEmail);
        }
    }

    @Test
    public void 커뮤니티_상세_정보() {
        final int communityId = 4;
        CommunityServiceManager.detail(communityId)
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

        assertNotNull(mDetailResponse.mResult);
        assertTrue(mDetailResponse.mResult.mId == communityId);
        assertNotNull(mDetailResponse.mResult.mAuthor);
        assertNotNull(mDetailResponse.mResult.mContent);
        assertNotNull(mDetailResponse.mResult.mCreated);
        assertTrue(mDetailResponse.mResult.mLikeCount >= 0);
        assertNotNull(mDetailResponse.mResult.mService);
        assertNotNull(mDetailResponse.mResult.mUpdated);

        assertTrue(mDetailResponse.mResult.mAuthor.mId >= 0);
        assertNotNull(mDetailResponse.mResult.mAuthor.mEmail);

        assertTrue(mDetailResponse.mResult.mService.mId >= 0);
        assertNotNull(mDetailResponse.mResult.mService.mTitle);
        assertNotNull(mDetailResponse.mResult.mService.mContent);
        assertNotNull(mDetailResponse.mResult.mService.mActiveDay);
        assertNotNull(mDetailResponse.mResult.mService.mEndDate);
        assertNotNull(mDetailResponse.mResult.mService.mFirestCategory);
        assertTrue(mDetailResponse.mResult.mService.mOrganiztionId >= 0);
    }

    @Test
    public void 커뮤니티_내용_작성() {

    }
}
