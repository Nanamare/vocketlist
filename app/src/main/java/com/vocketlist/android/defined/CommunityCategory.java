package com.vocketlist.android.defined;

import android.support.annotation.StringRes;

import com.vocketlist.android.R;

/**
 * Created by kinamare on 2017-02-20.
 */

public enum  CommunityCategory {
	All(R.string.com_all),
	MyPost(R.string.com_myWriting),
//	Wisdom(R.string.com_wisdom)
	;

	private final int resId;

	CommunityCategory(@StringRes int id) {
		this.resId = id;
	}

	public int getResId() {
		return resId;
	}
}
