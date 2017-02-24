package com.vocketlist.android.defined;

import android.support.annotation.StringRes;

import com.vocketlist.android.R;

/**
 * 카테고리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public enum Category {
    All(R.string.tab_all),
    Activity(R.string.tab_act),
    Education(R.string.tab_edu),
    Culture(R.string.tab_cul),
    Environment(R.string.tab_env),
//    Etc(R.string.tab_etc);

    ;

    private final int resId;

    Category(@StringRes int id) {
        this.resId = id;
    }

    public int getResId() {
        return resId;
    }
}