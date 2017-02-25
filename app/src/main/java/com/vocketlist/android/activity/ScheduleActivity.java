package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.ScheduleAdapter;
import com.vocketlist.android.dto.Schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 스케줄관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class ScheduleActivity extends DepthBaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_schedule) SuperRecyclerView mScheduleRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mScheduleRecyclerView.setAdapter(new ScheduleAdapter(getMock()));

        // todo api call
    }

    public List<Schedule> getMock() {
        List<Schedule> list = new ArrayList<>();

        list.add(createMockHeader());
        list.add(createMockItem());
        list.add(createMockItem());
        list.add(createMockHeader());
        list.add(createMockItem());
        list.add(createMockItem());


        return list;
    }

    private Schedule createMockItem() {
        Schedule schedule = new Schedule();

        schedule.mType = Schedule.ScheduleType.GROUP_ITEM;
        schedule.mStartDate = new Date(System.currentTimeMillis());
        schedule.mEndDate = new Date(System.currentTimeMillis() + (60 * 60 * 1000));
        schedule.mTitle = "주민센터 봉사";
        return schedule;
    }

    private Schedule createMockHeader() {
        Schedule schedule = new Schedule();

        schedule.mType = Schedule.ScheduleType.GROUP_HEADER;
        schedule.mHeaderTitle = "5월";

        return schedule;
    }
}
