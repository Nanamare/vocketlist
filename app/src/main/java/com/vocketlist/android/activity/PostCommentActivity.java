package com.vocketlist.android.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;

import com.binaryfork.spanny.Spanny;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.CommentAdapter;
import com.vocketlist.android.api.comment.CommentServiceManager;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.defined.Extras;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.helper.KeyboardHelper;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.manager.ToastManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 커뮤니티 : 댓글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class PostCommentActivity extends DepthBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnMoreListener,
        RecyclerViewItemClickListener
{
    private static final String TAG = PostCommentActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) SuperRecyclerView recyclerView;

    @BindView(R.id.tvCommentTarget) AppCompatTextView tvCommentTarget;
    @BindView(R.id.etComment) AppCompatEditText etComment;
    @BindView(R.id.btnComment) AppCompatButton btnComment;

    @BindString(R.string.comment_target) String commentTarget;

    private CommentAdapter adapter;

    private int postId = 0;
    private int page = 1;
    private int totalPage = 1;
    private boolean isChanged = false;

    private CommentListModel.Comment editComment;
    private CommentListModel.Comment parentComment;

    @OnTextChanged(value = R.id.etComment, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onCommentTextChanged(CharSequence s, int start, int before, int count) {
        btnComment.setEnabled(s.length() > 0);
    }

    @OnClick(R.id.btnComment)
    void onCommentClick(AppCompatButton v) {
        // 내용있음
        if(!TextUtils.isEmpty(btnComment.getText().toString().trim())) {
            // 대댓글
            if (parentComment != null) doChildAdd();
            // 수정
            else if(editComment != null) doModify();
            // 작성
            else doAdd();

            // 수정여부
            isChanged = true;

            clearCommentTarget();
            if(etComment.getEditableText() != null) etComment.getEditableText().clear();
        }
        // 내용없음
        else ToastManager.show(R.string.toast_invalid_content);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // 레이아웃 : 라사이클러
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshListener(this);
        recyclerView.setRefreshingColorResources(R.color.point_424C57, R.color.point_5FA9D0, R.color.material_white, R.color.point_E47B75);

        //
        handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    @Override
    public void finish() {
        if(isChanged) setResult(RESULT_OK, getIntent());
        super.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(Extras.SAVED, getIntent().getExtras());
        outState.putBoolean(Extras.IS_CHANGED, isChanged);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState.get(Extras.SAVED) != null) setIntent(new Intent().putExtras(savedInstanceState.getBundle(Extras.SAVED)));
        if(savedInstanceState.get(Extras.IS_CHANGED) != null) isChanged = savedInstanceState.getBoolean(Extras.IS_CHANGED);
    }

    @Override
    public void onRefresh() {
        reqList(page = 1);
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        if(page < totalPage) {
            reqList(++page);
            return;
        }

        recyclerView.hideMoreProgress();
    }

    @Override
    public void onItemClick(View v, int position) {

        parentComment = null;
        editComment = null;
        clearCommentTarget();

        Object o = v.getTag();
        if(o != null && o instanceof CommentListModel.Comment) {
            CommentListModel.Comment data = (CommentListModel.Comment) o;

            switch (v.getId()) {
                // 답글달기
                case R.id.btnComment: {
                    parentComment = data;
                    parentComment.position = position;
                    setCommentTarget(parentComment);
                    etComment.requestFocus();
                    KeyboardHelper.show(etComment);
                }
                break;

                // 수정
                case R.id.btnEdit: {
                    editComment = data;
                    editComment.position = position;
                    etComment.setText(data.mContent);
                    etComment.setSelection(etComment.getText().length());
                    etComment.requestFocus();
                    KeyboardHelper.show(etComment);

                    setMode(true);
                }
                break;

                // 삭제
                case R.id.btnDelete: {
                    doDelete(data, position);
                    // 수정여부
                    isChanged = true;
                }
                break;
            }
        }
    }

    /**
     * 인텐트 핸들링
     */
    private void handleIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null) {
                Serializable d = extras.getSerializable(Extras.DATA);
                Boolean isWrite = extras.getBoolean(Extras.IS_WRITE, false);
                if(d != null && d instanceof CommunityList.CommunityData) {
                    CommunityList.CommunityData data = (CommunityList.CommunityData) d;

                    // 글 아이디
                    postId = data.mId;

                    // 답글 있음
                    if(data.mComment != null && !data.mComment.isEmpty()) {
                        Collections.reverse(data.mComment);
                        adapter = new CommentAdapter(data.mComment, this);
                        reqList(1);
                    }
                    // 답글 없음
                    else adapter = new CommentAdapter(new ArrayList<>(), this);
                    recyclerView.setAdapter(adapter);

                    // 답글 쓰기 시
                    if(isWrite) etComment.requestFocus();
                }
            }
        }
    }

    /**
     * 댓글 대상 설정
     * @param comment
     */
    private void setCommentTarget(CommentListModel.Comment comment) {
        tvCommentTarget.setText(new Spanny(comment.mUserInfo.mName, new StyleSpan(Typeface.BOLD)).append(commentTarget));
        tvCommentTarget.setVisibility(View.VISIBLE);
    }

    /**
     * 댓글 대상 제거
     */
    private void clearCommentTarget() {
        if(tvCommentTarget.getEditableText() != null) tvCommentTarget.getEditableText().clear();
        tvCommentTarget.setVisibility(View.GONE);
    }

    /**
     * 모드 설정
     *
     * @param isModify
     */
    private void setMode(boolean isModify) {
        if(isModify) btnComment.setText(R.string.modify);
        else {
            editComment = null;
            btnComment.setText(R.string.post);
        }
    }

    /**
     * 작성
     */
    private void doAdd() {
        reqWrite(0, etComment.getText().toString());
    }

    /**
     * 대댓글
     */
    private void doChildAdd() {
        reqWrite(parentComment.mCommentId, etComment.getText().toString());
    }

    /**
     * 수정
     */
    private void doModify() {
        // TODO  대댓글 수정시 parent id ?
        reqModify(editComment.mCommentId, 0, etComment.getText().toString(), editComment.position);
    }

    /**
     * 삭제
     *
     * @param data
     * @param position
     */
    private void doDelete(CommentListModel.Comment data, int position) {
        reqDelete(data.mCommentId, position);
    }

    /**
     * 요청 : 목록
     *
     * @param page
     */
    private void reqList(int page) {
        CommentServiceManager.list(postId, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<BaseResponse<CommentListModel>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommentListModel>> baseResponseResponse) {
                        resList(baseResponseResponse.body());
                    }
                });
    }

    /**
     * 응답 : 목록
     *
     * @param response
     */
    private void resList(BaseResponse<CommentListModel> response) {
        if(response != null && response.mResult != null) {
            page = response.mResult.mPageIndex;
            totalPage = response.mResult.mPageCount;
            if (page == 1) adapter.setList(response.mResult.mCommentList);
            else adapter.addAll(response.mResult.mCommentList);
        }
    }

    /**
     * 요청 : 작성
     *
     * @param parentId
     * @param content
     */
    private void reqWrite(int parentId, String content) {
        CommentServiceManager.write(postId, parentId, content)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<BaseResponse<CommentListModel.Comment>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommentListModel.Comment>> baseResponseResponse) {
                        resWrite(baseResponseResponse.body());
                    }
                });
    }

    /**
     * 응답 : 작성
     *
     * @param response
     */
    private void resWrite(BaseResponse<CommentListModel.Comment> response) {
        if(response != null && response.mResult != null) {
            adapter.add(response.mResult);
            parentComment = null;

            // 하단 스크롤
            recyclerView.getRecyclerView().smoothScrollToPosition(adapter.getItemCount());
        }
    }

    /**
     * 요청 : 삭제
     *
     * @param id
     * @param position
     */
    private void reqDelete(int id, int position) {
        CommentServiceManager.delete(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<BaseResponse<Void>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
                        resDelete(position);

                    }
                });
    }

    /**
     * 응답 : 삭제
     *
     * @param position
     */
    private void resDelete(int position) {
        adapter.remove(position);
    }

    /**
     * 수정
     *
     * @param id
     * @param parentId
     * @param content
     * @param position
     */
    private void reqModify(int id, int parentId, String content, int position) {
        CommentServiceManager.modify(id, postId, parentId, content)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<BaseResponse<CommentListModel.Comment>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommentListModel.Comment>> baseResponseResponse) {
                        resModify(baseResponseResponse.body(), position);
                    }
                });
    }

    /**
     * 응답 : 수정
     *
     * @param response
     * @param position
     */
    private void resModify(BaseResponse<CommentListModel.Comment> response, int position) {
        if(response != null && response.mResult != null) {
            adapter.change(position, response.mResult);
            setMode(false);
        }
    }
}
