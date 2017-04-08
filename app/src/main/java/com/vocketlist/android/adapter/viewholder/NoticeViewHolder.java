package com.vocketlist.android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.api.notice.NoticeModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 뷰홀더 : 공지사항
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class NoticeViewHolder extends ChildViewHolder<NoticeModel.Notice> {

    @BindView(R.id.tvContent)
    AppCompatTextView tvContent;
    @BindView(R.id.ivPhoto)
    AppCompatImageView ivPhoto;
    @BindView(R.id.tvLink)
    AppCompatTextView tvLink;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public NoticeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * 데이터 바인딩
     *
     * @param notice
     */
    public void bind(NoticeModel.Notice notice) {
        // 내용
        if (!TextUtils.isEmpty(notice.mContent)) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(notice.mContent);
        } else tvContent.setVisibility(View.GONE);

        // 사진
        if (!TextUtils.isEmpty(notice.mPhoto)) {
            ivPhoto.setVisibility(View.VISIBLE);
            Glide.with(ivPhoto.getContext())
                    .load(notice.mPhoto)
                    .placeholder(R.drawable.ci_gray_small)
                    .error(R.drawable.ci_gray_small)
                    .centerCrop()
                    .crossFade()
                    .into(ivPhoto);
        } else ivPhoto.setVisibility(View.GONE);

        // 링크
        if (!TextUtils.isEmpty(notice.mLink)) {
            tvLink.setVisibility(View.VISIBLE);
            tvLink.setText(notice.mLink);
        } else tvLink.setVisibility(View.GONE);
    }
}
