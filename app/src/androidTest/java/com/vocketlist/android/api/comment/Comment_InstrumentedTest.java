package com.vocketlist.android.api.comment;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.api.comment.model.CommentDetailModel;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.api.comment.model.CommentWriteModel;
import com.vocketlist.android.api.community.CommunityServiceManager;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.dto.BaseResponse;
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

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */
@RunWith(AndroidJUnit4.class)
public class Comment_InstrumentedTest {
    private BaseResponse<CommunityList> mCommunityList;
    private BaseResponse<CommentListModel> mCommentList;
    private BaseResponse<CommentWriteModel> mCommentWrite;
    private BaseResponse<CommentDetailModel> mCommentDetail;

    @Before
    public void setup() {
        mCommunityList = null;
        mCommentWrite = null;
        mCommentList = null;
        mCommentDetail = null;

        ServiceDefine.mockInterceptor.setResponse(null);

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.immediate();
            }
        });

        UserServiceManager.autoLogin();
    }

    public void 커뮤니티_리스트_가져오기() {
        CommunityServiceManager.list(1, null)
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
                        mCommunityList = baseResponseResponse.body();
                    }

                });

        assertNotNull(mCommunityList);
        assertTrue(mCommunityList.mSuccess);
        assertNotNull(mCommunityList.mResult);
        assertTrue(mCommunityList.mResult.mData.size() > 0);
//        assertTrue(mCommunityList.mResult.mPageNumber == 1);

        for (CommunityList.CommunityData communityData : mCommunityList.mResult.mData) {
            assertNotNull(communityData.mUser);
//            assertNotNull(communityData.mContent);
            assertNotNull(communityData.mCreateDate);
            assertNotNull(communityData.mUpdateDate);

            assertTrue(communityData.mUser.mId >= 0);
            assertNotNull(communityData.mUser.mEmail);
        }
    }

    @Test
    public void step_1_댓글_작성() {
        커뮤니티_리스트_가져오기();

        CommentServiceManager.write(mCommunityList.mResult.mData.get(0).mId, 0, "댓글 Test")
                .subscribe(new Subscriber<Response<BaseResponse<CommentWriteModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommentWriteModel>> baseResponseResponse) {
                        mCommentWrite = baseResponseResponse.body();
                    }
                });

        assertNotNull(mCommentWrite);
        assertTrue(mCommentWrite.mSuccess);
    }

//    @Test
    public void step_2_댓글_리스트_가져오기() {
        커뮤니티_리스트_가져오기();

        CommentServiceManager.list(mCommunityList.mResult.mData.get(0).mId, 1)
                .subscribe(new Subscriber<Response<BaseResponse<CommentListModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommentListModel>> baseResponseResponse) {
                        mCommentList = baseResponseResponse.body();
                    }
                });

        assertNotNull(mCommentList);
        assertTrue(mCommentList.mSuccess);
        assertNotNull(mCommentList.mResult);
        assertNotNull(mCommentList.mResult.mCommentList);
        assertTrue(mCommentList.mResult.mCommentList.size() > 0);
        assertTrue(mCommentList.mResult.mCommentList.get(0).mCommentId > 0);
    }

//    @Test
    public void step_3_댓글_상세_정보() {
        step_2_댓글_리스트_가져오기();

        CommentServiceManager.detail(mCommentList.mResult.mCommentList.get(0).mCommentId)
                .subscribe(new Subscriber<Response<BaseResponse<CommentDetailModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommentDetailModel>> baseResponseResponse) {
                        mCommentDetail = baseResponseResponse.body();
                    }
                });

        assertNotNull(mCommentDetail);
        assertTrue(mCommentDetail.mSuccess);
        assertNotNull(mCommentDetail.mResult);
        assertTrue(mCommentDetail.mResult.mCommentId == mCommentList.mResult.mCommentList.get(0).mCommentId);
        assertNotNull(mCommentDetail.mResult.mContent);
    }

    //@Test
    public void setp_4_댓글_내용_갱신() {
        step_3_댓글_상세_정보();

        CommentServiceManager.modify(mCommentDetail.mResult.mCommentId, 0, "수정")
                .subscribe(new Subscriber<Response<BaseResponse<CommentWriteModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommentWriteModel>> baseResponseResponse) {
                        mCommentWrite = baseResponseResponse.body();
                    }
                });

        assertNotNull(mCommentWrite);
    }
}
