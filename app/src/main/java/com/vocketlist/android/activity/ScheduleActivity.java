package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.ScheduleAdapter;
import com.vocketlist.android.api.schedule.ScheduleListModel;
import com.vocketlist.android.api.schedule.ScheduleServiceManager;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.service.EmptySubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 스케줄관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class ScheduleActivity extends DepthBaseActivity implements
     SwipeRefreshLayout.OnRefreshListener
    , OnMoreListener
{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) SuperRecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // 레이아웃 : 라사이클러
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
//        recyclerView.addItemDecoration(new DividerInItemDecoration(this, lm.getOrientation())); // 구분선
        recyclerView.setRefreshListener(this);
        recyclerView.setRefreshingColorResources(R.color.point_424C57, R.color.point_5FA9D0, R.color.material_white, R.color.point_E47B75);
        recyclerView.setupMoreListener(this, 1);

        //
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

    /**
     * TODO 봉사활동 상세보기로 이동
     *
     * @param id 봉사활동 아이디
     */
    private void goToVolunteerDetail(int id) {

    }

    /**
     * 요청 : 목록
     *
     * @param page
     */
    private void reqList(int page) {
        showProgressDialog(true);

        ScheduleServiceManager.list()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        hideProgressDialog(true);
                    }
                })
                .subscribe(new EmptySubscriber<Response<BaseResponse<ScheduleListModel>>>() {
                    @Override
                    public void onNext(Response<BaseResponse<ScheduleListModel>> baseResponseResponse) {
                        resList(baseResponseResponse.body());
                    }
                });
    }

    /**
     * 응답 : 목록
     *
     * @param response
     */
    private void resList(BaseResponse<ScheduleListModel> response) {
        recyclerView.setAdapter(new ScheduleAdapter(response.mResult.mScheduleList));
    }

}