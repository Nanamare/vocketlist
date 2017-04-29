package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.ScheduleAdapter;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.api.schedule.ScheduleModel;
import com.vocketlist.android.api.schedule.ScheduleServiceManager;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Schedule;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.presenter.IView.IScheduleView;
import com.vocketlist.android.presenter.ipresenter.ISchedulePresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class ScheduleActivity extends DepthBaseActivity implements IScheduleView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_schedule) SuperRecyclerView mScheduleRecyclerView;

    private ISchedulePresenter presenter;
    private ScheduleAdapter adapter;
    private List<Schedule> scheduleList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

//        presenter = new SchedulePresenter(this);
        reqSchedule();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mScheduleRecyclerView.setAdapter(new ScheduleAdapter(getMock()));

        // todo api call
        adapter = new ScheduleAdapter(getMock());
    }

    private void reqSchedule() {
        ServiceDefine.mockInterceptor
                .setResponse("{\n" +
                        "  \"result\": {\n" +
                        "    \"links\": {\n" +
                        "      \"next\": 2,\n" +
                        "      \"previous\": -1\n" +
                        "    },\n" +
                        "    \"count\": 2,\n" +
                        "    \"page_count\": 1,\n" +
                        "    \"page_current\": 1,\n" +
                        "    \"page_size\": 20,\n" +
                        "    \"data\": [\n" +
                        "      {\n" +
                        "        \"id\": 1,\n" +
                        "        \"is_done\": false,\n" +
                        "        \"title\": \"볼런톤\",\n" +
                        "        \"start_date\": \"2017-12-30T14:00:23Z\",\n" +
                        "        \"end_date\": \"2017-12-30T15:00:00Z\",\n" +
                        "        \"start_time\": \"00:00:10\",\n" +
                        "        \"end_time\": \"00:00:15\",\n" +
                        "        \"place\" : \"한남동\"\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"id\": 3,\n" +
                        "        \"is_done\": true,\n" +
                        "        \"title\": \"청주문암장애인파크골프장 환경정리 및 주변정리\",\n" +
                        "        \"start_date\": \"2017-01-05T00:00:00Z\",\n" +
                        "        \"end_date\": \"2017-02-28T00:00:00Z\",\n" +
                        "        \"start_time\": \"00:00:10\",\n" +
                        "        \"end_time\": \"00:00:15\",\n" +
                        "        \"place\" : \"정자\"\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  \"success\": true,\n" +
                        "  \"message\": \"OK\"\n" +
                        "}");

        ScheduleServiceManager.getScheduleList()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        ServiceDefine.mockInterceptor.setResponse(null);
                    }
                })
                .subscribe(new EmptySubscriber<Response<BaseResponse<ScheduleModel>>>() {
                    @Override
                    public void onNext(Response<BaseResponse<ScheduleModel>> baseResponseResponse) {
                        ScheduleModel scheduleModel = baseResponseResponse.body().mResult;
                    }
                });
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

    @Override
    public void setScheduleList(BaseResponse<Schedule> scheduleList) {
        adapter.add(scheduleList.mResult);

    }
}
