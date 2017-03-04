package com.vocketlist.android.defined;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.R;

/**
 * 카테고리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public enum Category {
    @SerializedName("전체") All(R.string.tab_all, R.string.desc_all),
    @SerializedName("활동지원") Activity(R.string.tab_act, R.string.desc_activity),
    @SerializedName("교육상담") Education(R.string.tab_edu, R.string.desc_education),
    @SerializedName("문화체육") Culture(R.string.tab_cul, R.string.desc_culture),
    @SerializedName("환경") Environment(R.string.tab_env, R.string.desc_education);

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