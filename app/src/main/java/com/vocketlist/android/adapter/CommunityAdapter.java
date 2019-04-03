package com.vocketlist.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinamare on 2017-02-20.
 */

public class CommunityAdapter extends FragmentStatePagerAdapter {
	private final List<Fragment> mFragments = new ArrayList<>();
	private final List<String> mFragmentTitles = new ArrayList<>();

	/**
	 * 생성자
	 * @param fm
	 */
	public CommunityAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * 추가
	 * @param fragment
	 * @param title
	 */
	public void addFragment(Fragment fragment, String title) {
		mFragments.add(fragment);
		mFragmentTitles.add(title);
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
		return mFragmentTitles.get(position);
	}
}
