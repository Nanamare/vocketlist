package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.VolunteerCategoryAdapter;
import com.vocketlist.android.api.Link;
import com.vocketlist.android.api.vocket.VocketServiceManager;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.decoration.GridSpacingItemDecoration;
import com.vocketlist.android.defined.Args;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.roboguice.log.Ln;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindView;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;


/**
 * 봉사활동 : 카테고리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerCategoryFragment extends RecyclerFragment {
	@BindView(R.id.tvLabel) protected AppCompatTextView tvLabel;
	@BindInt(R.integer.volunteer_category_grid_column) protected int column;
	@BindDimen(R.dimen.volunteer_category_grid_space) protected int space;

	private VolunteerCategoryAdapter adapter;
	private Category category = Category.All;
	private String startDate;
	private String endDate;
	private int secondAddressId;
	private String searchKeyword;
	private boolean useVocketList = false;
	private Link link;

	private int pageSize = 1;
	private int page = 1;

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
		if (view == null) {
			return;
		}

		loadBundleData(savedInstanceState);

		initView();
		onRefresh();
	}

	private void initView() {
		tvLabel.setText(getString(category.getDescResId()));

		recyclerView.setAdapter(adapter = new VolunteerCategoryAdapter(new ArrayList<>()));
		recyclerView.addItemDecoration(new GridSpacingItemDecoration(column, space, false));

		ViewCompat.setNestedScrollingEnabled(recyclerView, false);
	}

	private void loadBundleData(Bundle savedInstanceState) {
		Bundle bundle = (savedInstanceState != null) ? savedInstanceState : getArguments();

		if (bundle == null) {
			return;
		}

		Serializable category = bundle.getSerializable(Args.CATEGORY);
		if (category != null) {
			this.category = (Category) category;
		}
	}

	private void requestVocketList(Category category,
								   String startDate,
								   String endDate,
								   int secondAddressId,
								   boolean useVocketFilter,
								   String searchKeyword,
								   int pageNum) {
//		if (recyclerView.isLoadingMore()) {
//			return;
//		}

		VocketServiceManager.search(category, startDate, endDate, secondAddressId, useVocketFilter, searchKeyword, pageNum)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideRefreshView();
					}
				})
				.subscribe(new Subscriber<Response<BaseResponse<Volunteer>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Ln.e(e, "getVocketList : " + e.toString());
					}

					@Override
					public void onNext(Response<BaseResponse<Volunteer>> baseResposeResponse) {
						setVocketList(baseResposeResponse.body());
					}
				});
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
		page = 1;
		requestVocketList(category, startDate, endDate, secondAddressId, useVocketList, searchKeyword, page);
	}

	@Override
	public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
		super.onMoreAsked(overallItemsCount, itemsBeforeMore, maxLastVisiblePosition);

		if (link == null
				|| link.mNextId == page
				|| link.mNextId > pageSize) {
			return;
		}

		requestVocketList(category, startDate, endDate, secondAddressId, useVocketList, searchKeyword, link.mNextId);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		resetSearch();
	}

	private void resetSearch() {
		startDate = null;
		endDate = null;
		secondAddressId = 0;
		useVocketList = false;
		searchKeyword = null;
	}

	public void setVocketList(BaseResponse<Volunteer> volunteerList) {
		if (volunteerList.mResult.mPageCurrent == 1) {
			adapter.clear();
		}
		adapter.addAll(volunteerList.mResult.mDataList);
		pageSize = volunteerList.mResult.mPageSize;
		page = volunteerList.mResult.mPageCurrent;
		link = volunteerList.mResult.mLink;
	}

	private void hideRefreshView() {
		recyclerView.setRefreshing(false);
		recyclerView.hideMoreProgress();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (TextUtils.isEmpty(query)) {
			Toast.makeText(getContext(), R.string.hint_search, Toast.LENGTH_SHORT).show();
			searchKeyword = null;
			return false;
		}

		searchKeyword = query;
		page = 1;

		requestVocketList(category, startDate, endDate, secondAddressId, useVocketList, searchKeyword, page);
		return true;
	}
}
