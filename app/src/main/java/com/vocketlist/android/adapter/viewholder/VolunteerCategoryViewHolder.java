package com.vocketlist.android.adapter.viewholder;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.activity.VolunteerReadActivity;
import com.vocketlist.android.api.vocket.Volunteer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 뷰홀더 : 봉사활동
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerCategoryViewHolder extends BaseViewHolder<Volunteer.Data> {

    @BindView(R.id.ivThumbnail) AppCompatImageView ivThumbnail;
    @BindView(R.id.tvTitle) AppCompatTextView tvTitle;
    @BindView(R.id.is_recruit_tv) AppCompatTextView tvRecruit;
    @BindView(R.id.start_date_tv) AppCompatTextView tvStartDate;
    @BindView(R.id.address_tv) AppCompatTextView tvAddress;
    @BindView(R.id.item_volunteer_ll) LinearLayout item_volunteer_ll;

    private Volunteer.Data mData;

    /**
     * 생성자
     * @param itemView
     */
    public VolunteerCategoryViewHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void bind(Volunteer.Data data) {
        mData = (Volunteer.Data)data;
        tvTitle.setText(mData.mTitle);
        tvStartDate.setText(getDate(mData.mStartDate));
        tvAddress.setText(mData.mFirstOffice);
        tvRecruit.setVisibility(mData.mIsActive ? View.VISIBLE : View.GONE);
        Glide.with(context).load("http://www.vocketlist.com"+mData.mImageUrl).into(ivThumbnail);
    }

    private String getDate(String date) {
        String[] list = date.split("[ T]");

        return list[0];
    }

    @OnClick(R.id.item_volunteer_ll)
    void tvTitleOnClick(){
      // 전달할값  mData.mId
        Intent intent = new Intent(context, VolunteerReadActivity.class);
        intent.putExtra("vocketId",String.valueOf(mData.mId));
        context.startActivity(intent);

    }


}
