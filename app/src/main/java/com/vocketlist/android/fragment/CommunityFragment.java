package com.vocketlist.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.activity.MainActivity;
import com.vocketlist.android.activity.PostCUActivity;
import com.vocketlist.android.adapter.CommunityAdapter;
import com.vocketlist.android.defined.CommunityCategory;
import com.vocketlist.android.roboguice.log.Ln;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 플래그먼트 : 커뮤니티
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class CommunityFragment extends BaseFragment {
    //
    @BindView(R.id.viewPager) ViewPager viewPager;

    //
    private CommunityAdapter mAdapter;

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
        mAdapter = new CommunityAdapter(getChildFragmentManager());
        for (CommunityCategory category : CommunityCategory.values()) {
            mAdapter.addFragment(CommunityCategoryFragment.newInstance(category), getString(category.getResId()));
        }
        viewPager.setAdapter(mAdapter);

        // 탭
        TabLayout tabLayout = ButterKnife.findById(act, R.id.tlCommunity);
        tabLayout.setupWithViewPager(viewPager);

        // TODO : 글쓰기
        AppCompatTextView btnFilter = ButterKnife.findById(getActivity(), R.id.btnWrite);
        btnFilter.setOnClickListener(v -> goToPostCreate());
    }

    /**
     * 글쓰기
     */
    private void goToPostCreate() {
        Intent intent = new Intent(getActivity(), PostCUActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
