package com.vocketlist.android.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.vocketlist.android.R;
import com.vocketlist.android.activity.MainActivity;
import com.vocketlist.android.adapter.VolunteerAdapter;
import com.vocketlist.android.api.vocket.VocketServiceManager;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.util.RxEventManager;
import com.vocketlist.android.view.LocalSelectView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 플래그먼트 : 봉사활동
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class VolunteerFragment extends BaseFragment {
    //
    @BindView(R.id.viewPager) ViewPager viewPager;

    //
    private VolunteerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_volunteer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity act = (MainActivity) getActivity();
        if (act == null) return;
        if (view == null) return;
        ButterKnife.bind(this, view);

        // 뷰페이저
        mAdapter = new VolunteerAdapter(getChildFragmentManager());
        for (Category category : Category.values()) {
            mAdapter.addFragment(VolunteerCategoryFragment.newInstance(category), getString(category.getTabResId()));
        }
        viewPager.setAdapter(mAdapter);

        // 탭
        TabLayout tabLayout = ButterKnife.findById(act, R.id.tlVolunteer);
        tabLayout.setupWithViewPager(viewPager);

        // TODO : 필터
        AppCompatImageButton btnFilter = ButterKnife.findById(getActivity(), R.id.btnFilter);
        btnFilter.setOnClickListener(v -> generateFilterLayout(v));
    }

    private void generateFilterLayout(View filterView) {

        LayoutInflater layoutInflater
            = LayoutInflater.from(getContext());
        View popupView = layoutInflater.inflate(R.layout.popup_filter, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(filterView,0,0);

        AppCompatTextView startTv  = (AppCompatTextView)popupView.findViewById(R.id.popup_filter_start_date_tv);
        AppCompatTextView endTv  = (AppCompatTextView)popupView.findViewById(R.id.popup_filter_end_date_tv);
//        LinearLayout layout = (LinearLayout)popupView.findViewById(R.id.popup_filter_layout);
        LocalSelectView localSelectView = (LocalSelectView) popupView.findViewById(R.id.local_select_view);
        AppCompatTextView localDoneBtn = (AppCompatTextView) popupView.findViewById(R.id.popup_filter_done_btn);

        startTv.setOnClickListener(view -> {
            com.vocketlist.android.util.TimePickerDialog dialog =
                com.vocketlist.android.util.TimePickerDialog.newInstance(view);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, "TimeDialog");
        });

        endTv.setOnClickListener(view -> {
            com.vocketlist.android.util.TimePickerDialog dialog =
                com.vocketlist.android.util.TimePickerDialog.newInstance(view);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, "TimeDialog");
        });

        localDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //카테고리 getter만들기 mAdapter.getItem(viewPager.getCurrentItem());
                int localDetailId =localSelectView.getLocalDetailId();
                String startDate = startTv.getText().toString();
                String endDate = endTv.getText().toString();
                int page = 1;
                if(isValid(localDetailId, startDate, endDate)){
                    VocketServiceManager.search(Category.All, startDate,
                        endDate, localDetailId, false, null, page)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Response<BaseResponse<Volunteer>>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Response<BaseResponse<Volunteer>> baseResponseResponse) {
                                RxEventManager.getInstance().sendData(baseResponseResponse.body());
                            }
                        });
                } else {
                    Toast.makeText(getContext(), getString(R.string.popup_filter_blank_alert), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isValid(int localDeatilNm, String startDate, String endDate) {
        if( localDeatilNm != 0
            && TextUtils.isEmpty(startDate) && TextUtils.isEmpty(endDate)){
            return false;
        } else {
            return true;
        }
    }


}
