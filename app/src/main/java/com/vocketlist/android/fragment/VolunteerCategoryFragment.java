package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.VolunteerCategoryAdapter;
import com.vocketlist.android.decoration.GridSpacingItemDecoration;
import com.vocketlist.android.defined.Args;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.Volunteer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.ButterKnife;


/**
 * 봉사활동 : 카테고리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerCategoryFragment extends RecyclerFragment {
    private VolunteerCategoryAdapter adapter;

    @BindInt(R.integer.volunteer_category_grid_column) int column;
    @BindDimen(R.dimen.volunteer_category_grid_space) int space;


    /**
     * 인스턴스
     *
     * @param catetory
     * @return
     */
    public static VolunteerCategoryFragment newInstance(Category catetory) {
        Bundle args = new Bundle();
        args.putSerializable(Args.CATEGORY, catetory);

        VolunteerCategoryFragment fragment = new VolunteerCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view == null) return;

        // 더미
        List<Volunteer> dummy = new ArrayList<>();
        for (int i = 0; i < Math.random() * 10; i++) {
            dummy.add(new Volunteer());
        }

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(column, space, true));
        recyclerView.setAdapter(adapter = new VolunteerCategoryAdapter(dummy));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_volunteer_category;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), column);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        // TODO 리프레시
        adapter.addAll(new ArrayList<Volunteer>());
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        super.onMoreAsked(overallItemsCount, itemsBeforeMore, maxLastVisiblePosition);

        // TODO 더보기
        adapter.add(new Volunteer());
    }
}
