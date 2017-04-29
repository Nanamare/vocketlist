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
import com.vocketlist.android.dto.Schedule;
import com.vocketlist.android.presenter.ipresenter.ISchedulePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private ISchedulePresenter presenter;
    private ScheduleAdapter adapter;
    private List<Schedule> scheduleList;

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
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // todo api call
//        adapter = new ScheduleAdapter(getMock());
        List<Schedule> schedules = new ArrayList<>();

        recyclerView.setAdapter(adapter = new ScheduleAdapter(schedules));
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

//    public List<Schedule> getMock() {
//        List<Schedule> list = new ArrayList<>();
//
//        list.add(createMockHeader());
//        list.add(createMockItem());
//        list.add(createMockItem());
//        list.add(createMockHeader());
//        list.add(createMockItem());
//        list.add(createMockItem());
//
//
//        return list;
//    }
//
//    private Schedule createMockItem() {
//        Schedule schedule = new Schedule();
//
//        schedule.mType = Schedule.ScheduleType.GROUP_ITEM;
//        schedule.mStartDate = new Date(System.currentTimeMillis());
//        schedule.mEndDate = new Date(System.currentTimeMillis() + (60 * 60 * 1000));
//        schedule.mTitle = "주민센터 봉사";
//        return schedule;
//    }
//
//    private Schedule createMockHeader() {
//        Schedule schedule = new Schedule();
//
//        schedule.mType = Schedule.ScheduleType.GROUP_HEADER;
//        schedule.mHeaderTitle = "5월";
//
//        return schedule;
//    }

    private void reqList(int page) {

    }

    private void resList() {
//        adapter.add(scheduleList.mResult);

    }
}