package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.CommentAdapter;
import com.vocketlist.android.dto.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private CommentAdapter adapter;
    private List<Comment> commentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            commentList = (List<Comment>) bundle.getSerializable("commentList");
        }

        // 더미
        List<Comment> dummy = new ArrayList<>();
        for (int i = 0; i < Math.random() * 15; i++) {
            dummy.add(new Comment());
        }

        // 레이아웃 : 라사이클러
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshListener(this);
        recyclerView.setRefreshingColorResources(R.color.point_424C57, R.color.point_5FA9D0, R.color.material_white, R.color.point_E47B75);
        recyclerView.setupMoreListener(this, 1);

        recyclerView.setAdapter(adapter = new CommentAdapter(dummy));

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
}
