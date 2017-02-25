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
    All(R.string.tab_all, R.string.desc_all),
    Activity(R.string.tab_act, R.string.desc_activity),
    Education(R.string.tab_edu, R.string.desc_education),
    Culture(R.string.tab_cul, R.string.desc_culture),
    Environment(R.string.tab_env, R.string.desc_education),
//    Etc(R.string.tab_etc);

    ;

    private final int tabResId;
    private final int descResId;

    Category(@StringRes int tabResid, @StringRes int descResId) {
        this.tabResId = tabResid;
        this.descResId = descResId;
    }

    public int getTabResId() {
        return tabResId;
    }

    public int getDescResId() {
        return descResId;
    }
}