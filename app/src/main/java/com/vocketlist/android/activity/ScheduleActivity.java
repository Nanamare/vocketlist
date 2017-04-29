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
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.api.schedule.ScheduleModel;
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
                        resList(baseResponseResponse.body());
                    }
                });
    }

    /**
     * 응답 : 목록
     *
     * @param response
     */
    private void resList(BaseResponse<ScheduleModel> response) {
        recyclerView.setAdapter(new ScheduleAdapter(response.mResult.mScheduleList));
    }

}