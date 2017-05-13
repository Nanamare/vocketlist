package com.vocketlist.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vocketlist.android.defined.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * 어댑터 : 봉사활동 : 카테고리 페이저
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 1. 13.
 */
public class VolunteerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTiles = new ArrayList<>();

    /**
     * 생성자
     * @param fm
     */
    public VolunteerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 추가
     * @param fragment
     * @param title
     */
    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTiles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTiles.get(position);
    }

}
