package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.NoticeAdapter;
import com.vocketlist.android.adapter.viewholder.ListItemNotice;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.api.notice.NoticeModel;
import com.vocketlist.android.api.notice.NoticeServiceManager;
import com.vocketlist.android.decoration.DividerInItemDecoration;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.roboguice.log.Ln;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 공지사항
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class NoticeActivity extends DepthBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener
        , OnMoreListener
{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) SuperRecyclerView recyclerView;

    private NoticeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // 레이아웃 : 라사이클러
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerInItemDecoration(this, lm.getOrientation())); // 구분선
        recyclerView.setRefreshListener(this);
        recyclerView.setRefreshingColorResources(R.color.point_424C57, R.color.point_5FA9D0, R.color.material_white, R.color.point_E47B75);
        recyclerView.setupMoreListener(this, 1);

        // 목록 요청
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
     * 요청 : 목록
     */
    private void reqList(int page) {
        String mockData = "{\n" +
                "  \"success\": true,\n" +
                "  \"result\": {\n" +
                "  \t\"data\": [\n" +
                "\t  \t{\n" +
                "\t\t  \t\"id\": 1,\n" +
                "\t\t  \t\"title\": \"첫번째 타이틀\",\n" +
                "\t\t  \t\"content\": \"내용\",\n" +
                "\t\t  \t\"photo\": \"https://source.unsplash.com/category/nature\",\n" +
                "\t\t  \t\"link\": \"http://www.naver.com\",\n" +
                "\t\t  \t\"timestamp\": \"2017.04.01\"\n" +
                "\t  \t},\n" +
                "\n" +
                "\t  \t{\n" +
                "\t\t  \t\"id\": 2,\n" +
                "\t\t  \t\"title\": \"첫번째 타이틀\",\n" +
                "\t\t  \t\"content\": \"내용\",\n" +
                "\t\t  \t\"photo\": \"https://source.unsplash.com/category/nature\",\n" +
                "\t\t  \t\"link\": \"http://www.naver.com\",\n" +
                "\t\t  \t\"timestamp\": \"2017.04.01\"\n" +
                "\t  \t}\n" +
                "  \t]\n" +
                "  },\n" +
                "  \"message\": \"success\"\n" +
                "}";

        ServiceDefine.mockInterceptor.setResponse(mockData);
        NoticeServiceManager.getNotice()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> ServiceDefine.mockInterceptor.setResponse(null))
                .subscribe(new Subscriber<Response<BaseResponse<NoticeModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Ln.e(e, "onError");
                    }

                    @Override
                    public void onNext(Response<BaseResponse<NoticeModel>> baseResponseResponse) {
                        resList(baseResponseResponse.body());
                    }
                });
    }

    /**
     * 응답 : 목록
     *
     * @param response
     */
    private void resList(BaseResponse<NoticeModel> response) {
        List<NoticeModel.Notice> notices = response.mResult.mNoticeList;
        List<ListItemNotice> item = new ArrayList<>();
        if(notices != null && !notices.isEmpty()) {
            for (NoticeModel.Notice notice : notices) {
                item.add(new ListItemNotice(notice));
            }
        }
        recyclerView.setAdapter(mAdapter = new NoticeAdapter(item));
    }
}
