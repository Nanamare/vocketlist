package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.activity.MainActivity;
import com.vocketlist.android.adapter.VolunteerAdapter;
import com.vocketlist.android.defined.Category;

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
    @BindView(R.id.viewPager)
    ViewPager viewPager;

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

        // 탭
        TabLayout tabLayout = (TabLayout) act.findViewById(R.id.tlVolunteer);

        // 뷰페이저
        mAdapter = new VolunteerAdapter(getChildFragmentManager());
        for (Category category : Category.values()) {
            mAdapter.addFragment(VolunteerCategoryFragment.newInstance(category), getString(category.getResId()));
//            tabLayout.addTab(tabLayout.newTab().setText(getString(category.getResId())));
        }
        viewPager.setAdapter(mAdapter);

        tabLayout.setupWithViewPager(viewPager);

        // 필터 탭 추가
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_action_clear_all));
//        // 이벤트
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            int prevPosition = 0;
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                // 필터 탭은 제외
//                if (position < mAdapter.getCount()) viewPager.setCurrentItem(position);
//                else viewPager.setCurrentItem(prevPosition);
//
//                prevPosition = position;
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }
}
