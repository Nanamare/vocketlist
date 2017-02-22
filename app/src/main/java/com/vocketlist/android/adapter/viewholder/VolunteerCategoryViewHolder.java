package com.vocketlist.android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.vocketlist.android.R;

import java.io.Serializable;

import butterknife.BindView;

/**
 * 뷰홀더 : 봉사활동
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerCategoryViewHolder extends BaseViewHolder {

    @BindView(R.id.ivThumbnail) AppCompatImageView ivThumbnail;
    @BindView(R.id.tvTitle) AppCompatTextView tvTitle;
    @BindView(R.id.is_recruit_tv) AppCompatTextView tvRecruit;
    @BindView(R.id.start_date_tv) AppCompatTextView tvStartDate;
    @BindView(R.id.address_tv) AppCompatTextView tvAddress;

    /**
     * 생성자
     * @param itemView
     */
    public VolunteerCategoryViewHolder(View itemView) {
        super(itemView);
    }

    @NonNull
    @Override
    public <T extends Serializable> void bind(T data) {

    }
}
