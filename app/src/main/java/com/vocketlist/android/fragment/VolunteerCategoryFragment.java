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
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.presenter.IView.IVolunteerCategoryView;
import com.vocketlist.android.presenter.VolunteerCategoryPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindInt;


/**
 * 봉사활동 : 카테고리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerCategoryFragment extends RecyclerFragment implements IVolunteerCategoryView {
	private VolunteerCategoryAdapter adapter;

	private IVolunteerCategoryPresenter presenter;

	private int page = 1;
	private Volunteer.Link link;

	@BindInt(R.integer.volunteer_category_grid_column)
	int column;
	@BindDimen(R.dimen.volunteer_category_grid_space)
	int space;


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
		if (view == null) return;

		presenter = new VolunteerCategoryPresenter(this);

		recyclerView.addItemDecoration(new GridSpacingItemDecoration(column, space, true));
		recyclerView.setAdapter(adapter = new VolunteerCategoryAdapter(new ArrayList<>()));
		presenter.getVoketList(page);
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
		presenter.getVoketList(1);
	}

	@Override
	public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
		super.onMoreAsked(overallItemsCount, itemsBeforeMore, maxLastVisiblePosition);

		if (link.next != null) {
			presenter.getVoketList(Integer.valueOf(link.next));
		}
	}

	@Override
	public void getVoketList(BaseResponse<Volunteer> volunteerList) {

		if (page == 1) {
			adapter.clear();
		}
		adapter.addAll(volunteerList.mResult.mDataList);
		page = volunteerList.mResult.mPageCurrent;
		link = volunteerList.mResult.mLink;
	}
}
