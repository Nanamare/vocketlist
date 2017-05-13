package com.vocketlist.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.VolunteerSearchAdapter;
import com.vocketlist.android.api.vocket.VocketServiceManager;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.decoration.DividerInItemDecoration;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.service.EmptySubscriber;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by SeungTaek.Lim on 2017. 5. 13..
 */

public class SearchVolunteerDialog extends Dialog implements SearchView.OnQueryTextListener {
    @BindView(R.id.dialog_volunteer_search_recyclerView) protected SuperRecyclerView mSearchRecycleView;
    @BindView(R.id.dialog_volunteer_search_view) protected SearchView mSearchView;

    @BindDimen(R.dimen.font_42) int searchFontSize;

    protected VolunteerSearchAdapter mSearchAdapter;
    private SearchQueryRunnable mSearchQueryRunnable;
    private Handler mSearchQueryHandler = new Handler();

    public SearchVolunteerDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_search);

        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
		theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_PX, searchFontSize);
		theTextArea.requestFocus();
        mSearchView.requestFocus();


        mSearchView.setOnQueryTextListener(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mSearchRecycleView.setLayoutManager(layoutManager);
        mSearchRecycleView.addItemDecoration(new DividerInItemDecoration(getContext(), layoutManager.getOrientation())); // 구분선

        // 데이터 설정
        mSearchAdapter = new VolunteerSearchAdapter(null);
        mSearchRecycleView.setAdapter(mSearchAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // 실시간으로 처리하지 말고 재입력 시간 Delay 후에 서버에 쿼리 요청
        if (mSearchQueryRunnable != null) {
            mSearchQueryHandler.removeCallbacks(mSearchQueryRunnable);
        }

        mSearchQueryHandler.postDelayed(
                mSearchQueryRunnable = new SearchQueryRunnable(newText),
                500
        );

        return false;
    }

    private class SearchQueryRunnable implements Runnable {
        private String query;

        /**
         * 생성자
         *
         * @param query
         */
        public SearchQueryRunnable(String query) {
            this.query = query;
        }

        @Override
        public void run() {
            reqVolunteers(query);
        }
    }

    private void reqVolunteers(String query) {
        VocketServiceManager.search(null, null, null, 0, false, query, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EmptySubscriber<Response<BaseResponse<Volunteer>>>() {
                    @Override
                    public void onNext(Response<BaseResponse<Volunteer>> baseResponseResponse) {
                        if (mSearchAdapter == null) {
                            return;
                        }

                        mSearchAdapter.setList(baseResponseResponse.body().mResult.mDataList);
                        mSearchAdapter.notifyDataSetChanged();
                    }
                });
    }

}
