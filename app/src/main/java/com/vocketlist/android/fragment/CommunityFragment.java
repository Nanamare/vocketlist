package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.PostAdapter;
import com.vocketlist.android.dto.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * 플래그먼트 : 커뮤니티
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class CommunityFragment extends RecyclerFragment {
    private PostAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view == null) return;

        // 더미
        List<Post> dummy = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dummy.add(new Post());
        }

        //
        recyclerView.setAdapter(adapter = new PostAdapter(dummy));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        // TODO 리프레시
        adapter.addAll(new ArrayList<Post>());
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        super.onMoreAsked(overallItemsCount, itemsBeforeMore, maxLastVisiblePosition);

        // TODO 더보기
        adapter.add(new Post());
    }
}
