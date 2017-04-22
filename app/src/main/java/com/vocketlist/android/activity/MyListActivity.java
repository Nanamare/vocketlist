package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.MyListAdapter;
import com.vocketlist.android.api.my.MyListModel;
import com.vocketlist.android.api.my.MyListServiceManager;
import com.vocketlist.android.decoration.DividerInItemDecoration;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.roboguice.log.Ln;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
	@BindView(R.id.ibPrevYear) AppCompatImageButton ibPrevYear;
	@BindView(R.id.tvYear) AppCompatTextView tvYear;
	@BindView(R.id.ibNextYear) AppCompatImageButton ibNextYear;

	@OnClick(R.id.ibPrevYear)
	void onPrevYearClick() {
		mCalendar.add(Calendar.YEAR, -1);

		// TODO 임시 - 나중에 서버 응답 후 OK면 갱신
		updateYear();
		reqList(mCalendar.get(Calendar.YEAR), 1);
	}

	@OnClick(R.id.ibNextYear)
	void onNextYearClick() {
		mCalendar.add(Calendar.YEAR, 1);

		// TODO 임시 - 나중에 서버 응답 후 OK면 갱신
		updateYear();
		reqList(mCalendar.get(Calendar.YEAR), 1);
	}

	private Calendar mCalendar;
	private MyListAdapter mAdapter;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_list);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		// 레이아웃 : 라사이클러
		LinearLayoutManager lm = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(lm);
		recyclerView.addItemDecoration(new DividerInItemDecoration(this, lm.getOrientation())); // 구분선
		recyclerView.setRefreshListener(this);
		recyclerView.setRefreshingColorResources(R.color.point_424C57, R.color.point_5FA9D0, R.color.material_white, R.color.point_E47B75);
		recyclerView.setupMoreListener(this, 1);

		//
		mCalendar = Calendar.getInstance(Locale.getDefault());
		updateYear();

		//
		reqList(mCalendar.get(Calendar.YEAR), 1);
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
				Object d = v.getTag();

				if(d instanceof MyListModel.MyList) {
					MyListModel.MyList data = (MyListModel.MyList) d;

					PopupMenu popup = new PopupMenu(v.getContext(), v, GravityCompat.END, R.attr.actionOverflowMenuStyle, 0);
					popup.inflate(R.menu.certification_modify_delete);
					popup.setOnMenuItemClickListener(item -> {
						int id = item.getItemId();
						switch (id) {
							case R.id.action_certification: doCertification(data); break;
							case R.id.action_modify: doModify(data); break;
							case R.id.action_delete: doDelete (data); break;
						}
						return true;
					});
					popup.show();
				}
			} break;

			// 기본
			default: {

			} break;
		}
	}

	/**
	 * 년도 갱신
	 */
	private void updateYear() {
		tvYear.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));
	}

	/**
	 * 인증
	 * @param data
	 */
	private void doCertification(MyListModel.MyList data) {

	}

	/**
	 * 수정
	 * @param data
	 */
	private void doModify(MyListModel.MyList data) {

	}

	/**
	 * 삭제
	 * @param data
	 */
	private void doDelete(MyListModel.MyList data) {

	}

	/**
	 * 요청 : 목록
	 * @param year
	 * @param page
	 */
    private void reqList(int year, int page) {
        MyListServiceManager.get(year, page)
				.observeOn(AndroidSchedulers.mainThread())
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

	/**
	 * 응답 : 목록
	 * @param response
	 */
	private void resList(BaseResponse<MyListModel> response) {
		List<MyListModel.MyList> myLists = response.mResult.mMyListList;
		if (myLists != null)
			recyclerView.setAdapter(mAdapter = new MyListAdapter(myLists, this));
		else recyclerView.hideProgress();
	}

	private void reqAdd(String content) {
		MyListServiceManager.write(content, false)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new EmptySubscriber<Response<BaseResponse<MyListModel.MyList>>>() {
					@Override
					public void onNext(Response<BaseResponse<MyListModel.MyList>> baseResponseResponse) {
						mAdapter.add(baseResponseResponse.body().mResult);
					}
				});
	}

	private void reqDelete(int id) {
		MyListServiceManager.delete(id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new EmptySubscriber<Response<BaseResponse<Void>>>() {
					@Override
					public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
						// todo : 리스트에서 삭제하는 로직 필요
					}
				});
	}

	private void reqModify(int id, String contents) {
		MyListServiceManager.modify(id, contents, false)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new EmptySubscriber<Response<BaseResponse<MyListModel.MyList>>>() {
					@Override
					public void onNext(Response<BaseResponse<MyListModel.MyList>> baseResponseResponse) {
						// todo : 리스트 갱신하는 로직 필요
					}
				});
	}
}
