package com.vocketlist.android.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
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
    @BindView(R.id.start_date_and_place_tv) AppCompatTextView tvStartDate;
    @BindView(R.id.item_volunteer_ll) LinearLayout item_volunteer_ll;

    private Context mContext;
    private Volunteer.Data mData;

    /**
     * 생성자
     * @param itemView
     */
    public VolunteerCategoryViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
    }

    @Override
    public void bind(Volunteer.Data data) {
        mData = data;

        // 이미지
        if(!TextUtils.isEmpty(data.mImageUrl)) {
            Glide.with(ivThumbnail.getContext())
                    .load(mContext.getString(R.string.vocket_base_url) + mData.mImageUrl)
                    .crossFade()
                    .centerCrop()
                    .into(ivThumbnail);
        }
        // 모집중 표시
        tvRecruit.setVisibility(mData.mIsActive ? View.VISIBLE : View.GONE);
        // 제목
        tvTitle.setText(mData.mTitle);
        // 날짝
        tvStartDate.setText(getDate(mData.mStartDate) + (TextUtils.isEmpty(mData.mFirstOffice) ? "" : (" / " + mData.mFirstOffice)));
    }

    private String getDate(String date) {
        String[] list = date.split("[ T]");

        return list[0];
    }

    @OnClick(R.id.item_volunteer_ll)
    void tvTitleOnClick(){
      // 전달할값  mData.mId
        Intent intent = new Intent(context, VolunteerReadActivity.class);
        intent.putExtra(VolunteerReadActivity.EXTRA_KEY_VOCKET_ID, String.valueOf(mData.mId));
        context.startActivity(intent);

    }


}
