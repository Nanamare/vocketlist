package com.vocketlist.android.adapter.viewholder;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.vocketlist.android.R;
import com.vocketlist.android.activity.VolunteerReadActivity;
import com.vocketlist.android.api.schedule.ScheduleListModel;
import com.vocketlist.android.util.DateUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;


/**
 * 뷰홀더 : 스케쥴
 */
public class ScheduleViewHolder extends BaseViewHolder<ScheduleListModel.Schedule> {

    @BindView(R.id.tvDate) AppCompatTextView tvDate;
    @BindView(R.id.llInfo) LinearLayout llInfo;
    @BindView(R.id.tvLabel) AppCompatTextView tvLabel;
    @BindView(R.id.tvTime) AppCompatTextView tvTime;

    /**
     * 생성자
     *
     * @param itemView
     */
    public ScheduleViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(ScheduleListModel.Schedule data) {
        super.bind(data);

        //
        tvDate.setText(DateUtil.convertDateForSchedule(data.mStartDate) + "\n-\n" + DateUtil.convertDateForSchedule(data.mEndDate));
        llInfo.setSelected(data.mIsDone);
        tvLabel.setText(data.mTitle);
        tvTime.setText(DateUtil.convertTimeForSchedule(data.mStartTime) + " ~ " + DateUtil.convertTimeForSchedule(data.mEndTime) + " / " + data.mArea);
    }

    @OnClick(R.id.llInfo)
    protected void onClickInfoLayer(View view) {
        Intent intent = new Intent(view.getContext(), VolunteerReadActivity.class);
        intent.putExtra(VolunteerReadActivity.EXTRA_KEY_VOCKET_ID, String.valueOf(data.mServiceId));
        context.startActivity(intent);
    }

    @OnLongClick(R.id.llInfo)
    protected boolean onLongClickInfoLayer(View view) {
        return false;
    }
}