package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.PostAdapter;
import com.vocketlist.android.api.Link;
import com.vocketlist.android.api.community.CommunityServiceManager;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.defined.Args;
import com.vocketlist.android.defined.CommunityCategory;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.presenter.IView.ICommunityView;
import com.vocketlist.android.presenter.ipresenter.ICommunityPresenter;
import com.vocketlist.android.roboguice.log.Ln;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by kinamare on 2017-02-20.
 */

/**
 * 커뮤니티 : 카테고리
 */
public class CommunityCategoryFragment extends RecyclerFragment implements ICommunityView, RecyclerViewItemClickListener {

	private PostAdapter adapter;
	private ICommunityPresenter presenter;
	private Link links;
	private int communityListPgCnt = 1;
	private int page = 1;
	/**
	 * 인스턴스
	 *
	 * @param catetory
	 * @return
	 */
	public static CommunityCategoryFragment newInstance(CommunityCategory catetory) {
		Bundle args = new Bundle();
		args.putSerializable(Args.CATEGORY, catetory);

		CommunityCategoryFragment fragment = new CommunityCategoryFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(view == null) return;

		Bundle args = getArguments();
		Serializable c = args.getSerializable(Args.CATEGORY);
		if (c != null && c instanceof CommunityCategory) {
			CommunityCategory category = (CommunityCategory) c;

			recyclerView.setAdapter(adapter = new PostAdapter(new ArrayList<>()));
			requestCommunityList(communityListPgCnt++);
		}
	}

	private void requestCommunityList(int pageNum) {
		CommunityServiceManager.list(pageNum)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideRefreshView();
					}
				})
				.subscribe(new Subscriber<Response<BaseResponse<CommunityList>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Ln.e(e, "getCommunityList : " + e.toString());
					}

					@Override
					public void onNext(Response<BaseResponse<CommunityList>> baseResponseResponse) {
						setCommunityList(baseResponseResponse.body());
					}
				});

	}

	private void hideRefreshView() {
		recyclerView.setRefreshing(false);
		recyclerView.hideMoreProgress();
	}

	private void setCommunityList(BaseResponse<CommunityList> communityList) {
		adapter.addAll(communityList.mResult.mData);
		page = communityList.mResult.mPageCurrentCnt;
		links = communityList.mResult.mLinks;
	}


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_community_category;
	}

	@Override
	protected RecyclerView.LayoutManager getLayoutManager() {
		return new LinearLayoutManager(getContext());
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

	@Override
	public void onItemClick(View v, int position) {

	}
}
