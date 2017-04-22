package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.MyListAdapter;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.api.my.MyListModel;
import com.vocketlist.android.api.my.MyListServiceManager;
import com.vocketlist.android.decoration.DividerInItemDecoration;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.roboguice.log.Ln;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 목표관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class MyListActivity extends DepthBaseActivity implements
	SwipeRefreshLayout.OnRefreshListener
	, OnMoreListener
	, RecyclerViewItemClickListener
{
	@BindView(R.id.toolbar)	Toolbar toolbar;
	@BindView(R.id.recyclerView) SuperRecyclerView recyclerView;

	private MyListAdapter mAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylist);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		// 레이아웃 : 라사이클러
		LinearLayoutManager lm = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(lm);
		recyclerView.addItemDecoration(new DividerInItemDecoration(this, lm.getOrientation())); // 구분선
		recyclerView.setRefreshListener(this);
		recyclerView.setRefreshingColorResources(R.color.point_424C57, R.color.point_5FA9D0, R.color.material_white, R.color.point_E47B75);
		recyclerView.setupMoreListener(this, 1);

		reqList(1);
	}

	@Override
	public void onRefresh() {
		recyclerView.setRefreshing(false);
		recyclerView.hideProgress();
	}

	@Override
	public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
		recyclerView.hideMoreProgress();
	}

	@Override
	public void onItemClick(View v, int position) {
		switch (v.getId()) {
			// 더보기
			case R.id.ibMore: {

			} break;

			// 기본
			default: {

			} break;
		}
	}

	private void goToCommunity() {

	}

	private void reqList(int page) {
		String mockData = "{\n" +
				"  \"success\": true,\n" +
				"  \"message\": \"sucess\",\n" +
				"  \"result\": {\n" +
				"    \"data\": [\n" +
				"      {\n" +
				"        \"id\": 0,\n" +
				"        \"content\": \"6개월이상 텀블러 사용하기\",\n" +
				"        \"is_done\": true\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": 1,\n" +
				"        \"content\": \"6개월이상 텀블러 사용하기\",\n" +
				"        \"is_done\": false\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": 2,\n" +
				"        \"content\": \"6개월이상 텀블러 사용하기\",\n" +
				"        \"is_done\": true\n" +
				"      },\n" +
				"      {\n" +
				"        \"id\": 3,\n" +
				"        \"content\": \"6개월이상 텀블러 사용하기\",\n" +
				"        \"is_done\": true\n" +
				"      }\n" +
				"    ]\n" +
				"  }\n" +
				"}";

		ServiceDefine.mockInterceptor.setResponse(mockData);
		MyListServiceManager.get(page)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(() -> ServiceDefine.mockInterceptor.setResponse(null))
				.subscribe(new Subscriber<Response<BaseResponse<MyListModel>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Ln.e(e, "onError");
					}

					@Override
					public void onNext(Response<BaseResponse<MyListModel>> baseResponseResponse) {
						resList(baseResponseResponse.body());
					}
				});
	}

	private void resList(BaseResponse<MyListModel> response) {
		List<MyListModel.MyList> myLists = response.mResult.mMyListList;
		if (myLists != null)
			recyclerView.setAdapter(mAdapter = new MyListAdapter(myLists, this));
		else recyclerView.hideProgress();
	}

	private void reqAdd() {

	}

	private void reqDelete() {

	}

	private void reqModify() {

	}
}
