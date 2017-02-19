package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 플래그먼트 : 라시이클러
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public abstract class RecyclerFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener
        , OnMoreListener
{
    @BindView(R.id.recyclerView) SuperRecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view == null) return;
        ButterKnife.bind(this, view);

        // 레이아웃 : 라사이클러
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setRefreshListener(this);
        recyclerView.setRefreshingColorResources(R.color.point_343a45, R.color.point_5ebdba, R.color.point_f6b53c, R.color.point_ea5e58);
        recyclerView.setupMoreListener(this, 1);
    }

    @Override
    public void onRefresh() {
        // Do nothing
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        // Do nothing
    }


    /**
     * 레이아웃 아이디
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 레이아웃 매니져
     * @return
     */
    @NonNull
    protected abstract RecyclerView.LayoutManager getLayoutManager();
}
