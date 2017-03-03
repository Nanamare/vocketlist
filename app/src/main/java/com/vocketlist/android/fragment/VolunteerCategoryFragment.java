package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.VolunteerCategoryAdapter;
import com.vocketlist.android.decoration.GridSpacingItemDecoration;
import com.vocketlist.android.defined.Args;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Link;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.presenter.IView.IVolunteerCategoryView;
import com.vocketlist.android.presenter.VolunteerCategoryPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindView;


/**
 * 봉사활동 : 카테고리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerCategoryFragment extends RecyclerFragment implements IVolunteerCategoryView {
	@BindView(R.id.tvLabel)
	AppCompatTextView tvLabel;

	@BindInt(R.integer.volunteer_category_grid_column)
	int column;
	@BindDimen(R.dimen.volunteer_category_grid_space)
	int space;

	private VolunteerCategoryAdapter adapter;
	private IVolunteerCategoryPresenter presenter;
	private Category category;

	private int page = 1;
	private Link link;

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

		Bundle args = getArguments();
		if (args != null) {
			Serializable c = args.getSerializable(Args.CATEGORY);
			if (c != null && c instanceof Category) {
				category = (Category) c;

				// 카테고리 설명
				tvLabel.setText(getString(category.getDescResId()));

				presenter = new VolunteerCategoryPresenter(this);

				recyclerView.setAdapter(adapter = new VolunteerCategoryAdapter(new ArrayList<>()));
				recyclerView.addItemDecoration(new GridSpacingItemDecoration(column, space, true));
				ViewCompat.setNestedScrollingEnabled(recyclerView, false);

				if (getString(category.getTabResId()).equals("전체")) {
					presenter.getVoketList(page);
				} else {
					presenter.getVocketCategoryList(getString(category.getTabResId()), 1);
				}


			}
		}
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
		if (getString(category.getTabResId()).equals("전체")) {
			presenter.getVoketList(1);
		} else {
			presenter.getVocketCategoryList(getString(category.getTabResId()), 1);
		}
	}

	@Override
	public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
		super.onMoreAsked(overallItemsCount, itemsBeforeMore, maxLastVisiblePosition);

		if (link.mNext >= 0) {
			if (getString(category.getTabResId()).equals("전체")) {
				presenter.getVoketList(Integer.valueOf(link.mNext));
			} else {
				presenter.getVocketCategoryList(getString(category.getTabResId()), Integer.valueOf(link.mNext));
			}
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

	@Override
	public void destorySwipeView() {


	}


}
