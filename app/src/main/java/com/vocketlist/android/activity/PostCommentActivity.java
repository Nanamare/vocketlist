package com.vocketlist.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.CommentAdapter;
import com.vocketlist.android.api.Link;
import com.vocketlist.android.api.comment.CommentServiceManager;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.api.comment.model.CommentWriteModel;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Comment;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 커뮤니티 : 댓글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class PostCommentActivity extends DepthBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener
        , OnMoreListener
{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.activity_post_comment_recyclerView) SuperRecyclerView recyclerView;
    @BindView(R.id.activity_post_comment_tv) AppCompatEditText contentsEdt;
    @BindView(R.id.activity_post_comment_sendBtn) AppCompatTextView sendBtn;

    private BaseResponse<CommentWriteModel> commentModel;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private int roomId = 0;
    private int postPageCnt = 1;
    private int page = 1;
    private Link link;
    private boolean isCheckBlank;
    private BaseResponse<CommentListModel> commentListModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            commentList = (List<Comment>) bundle.getSerializable("commentList");
            Intent intent = getIntent();
            if(intent != null) roomId = intent.getExtras().getInt("CommunityRoomId");
        }

        // 레이아웃 : 라사이클러
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshListener(this);
        recyclerView.setRefreshingColorResources(R.color.point_424C57, R.color.point_5FA9D0, R.color.material_white, R.color.point_E47B75);
        recyclerView.setAdapter(adapter = new CommentAdapter(new ArrayList<>(),listener));
        requestCommentList();


        //댓글아이콘을 클릭했을때는 포커스온
        Intent intent = getIntent();
        if(intent != null){
            if(R.id.btnComment == intent.getExtras().getInt("viewId")){
                contentsEdt.requestFocus();
            }
        }

        checkCommentsBlank(contentsEdt);
    }

    private void checkCommentsBlank(AppCompatEditText contentsTv) {
        Observable<CharSequence> primaryBtn = RxTextView.textChanges(contentsTv);
        primaryBtn.map(charSequence -> charSequence.length() > 0).subscribe(aBoolean -> {
            isCheckBlank = aBoolean;
            if(isCheckBlank){
                sendBtn.setTextColor(getResources().getColor(R.color.point_5FA9D0));
            }else {
                sendBtn.setTextColor(getResources().getColor(R.color.gray_8080));
            }
        });
    }

    private void requestCommentList() {
//        roomId, postPageCnt
        CommentServiceManager.list(55, 1)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate(new Action0() {
                @Override
                public void call() {

                }
            })
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
                    commentListModel = baseResponseResponse.body();
                    setCommentList(baseResponseResponse.body());
                }
            });

    }

    private void setCommentList(BaseResponse<CommentListModel> commentList) {
        adapter.clear();
        adapter.addAll(commentList.mResult.mCommentList);
        link = commentList.mResult.mLink;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        // TODO 리프레시
        adapter.addAll(new ArrayList<Comment>());
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        // TODO 더보기
        adapter.add(new Comment());
    }



    @OnClick(R.id.activity_post_comment_sendBtn)
    void sendBtn(){
        String content = contentsEdt.getText().toString();
        if(content.length() == 0 ){
            Toast.makeText(this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            CommentServiceManager.write(55,0,content)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Subscriber<Response<BaseResponse<CommentWriteModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<BaseResponse<CommentWriteModel>> baseResponseResponse) {
                        commentModel = baseResponseResponse.body();
                        clearTextView();
                        updateCommentList(commentModel);
                    }
                });
        }
    }

    private void updateCommentList(BaseResponse<CommentWriteModel> commentModel) {

        CommentListModel.Comment comment = new CommentListModel.Comment();
        comment.mContent = commentModel.mResult.mContent;
        comment.mCommentId = commentModel.mResult.mCommentId;
        comment.mTimestamp = commentModel.mResult.mTimestamp;
        comment.mUserInfo = commentModel.mResult.mUserInfo;

        adapter.add(comment);
        adapter.notifyDataSetChanged();
    }

    private void clearTextView() {
        contentsEdt.setText("");
    }


    @Override
    protected void onResume(){
        super.onResume();
        requestCommentList();
    }

    RecyclerViewItemClickListener listener = new RecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()){
                case R.id.civPhoto:{
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestDeleteComment(position);
                        }
                    });
                }
            }
        }
    };

    private void requestDeleteComment(int position) {
//        ((CommentListModel.Comment)adapter.getItem(position)).mCommentId 이게 올바른 로직
//         commentListModel.mResult.mCommentList.get(position).mCommentId
        CommentServiceManager.delete(((CommentListModel.Comment)adapter.getItem(position)).mCommentId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate(new Action0() {
                @Override
                public void call() {

                }
            })
            .subscribe(new Subscriber<Response<BaseResponse<Void>>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    adapter.remove(position);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
                    Toast.makeText(PostCommentActivity.this, "완료", Toast.LENGTH_SHORT).show();
                    adapter.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

    }
}
