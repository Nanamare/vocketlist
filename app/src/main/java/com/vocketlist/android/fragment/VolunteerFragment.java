package com.vocketlist.android.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.vocketlist.android.R;
import com.vocketlist.android.activity.MainActivity;
import com.vocketlist.android.adapter.VolunteerAdapter;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.view.LocalSelectView;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        LinearLayout layout = (LinearLayout)popupView.findViewById(R.id.popup_filter_layout);
        LocalSelectView localSelectView = (LocalSelectView) popupView.findViewById(R.id.local_select_view);
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


    }
}
