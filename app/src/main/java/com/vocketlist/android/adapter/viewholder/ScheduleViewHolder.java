package com.vocketlist.android.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.vocketlist.android.R;
import com.vocketlist.android.api.schedule.ScheduleModel;
import com.vocketlist.android.util.DateUtil;

import butterknife.BindView;


/**
 * 뷰홀더 : 스케쥴
 */
public class ScheduleViewHolder extends BaseViewHolder<ScheduleModel.Schedule> {

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
    public void bind(ScheduleModel.Schedule data) {
        super.bind(data);

        //
        tvDate.setText(DateUtil.convertDateForSchedule(data.mStartDate) + "\n-\n" + DateUtil.convertDateForSchedule(data.mEndDate));
        llInfo.setSelected(data.mIsDone);
        tvLabel.setText(data.mTitle);
        tvTime.setText(DateUtil.convertTimeForSchedule(data.mStartTime) + " ~ " + DateUtil.convertTimeForSchedule(data.mEndTime) + " / " + data.mArea);
    }
}

/**
 * Created by SeungTaek.Lim on 2017. 2. 25..
 */

/**
public class ScheduleViewHolder extends BaseViewHolder<Schedule> {
    private Schedule mSchedule;

    protected TextView mHeader;
    protected TextView mDate;
    protected TextView mContent;
    protected TextView mTime;
    private int durationTime;

    private ScheduleViewHolder(View view) {
        super(view);

        mHeader = ButterKnife.findById(view, R.id.tv_schedule_header);
        mDate = ButterKnife.findById(view, R.id.tv_schedule_date);
        mContent = ButterKnife.findById(view, R.id.tv_schedule_content);
        mTime = ButterKnife.findById(view, R.id.tv_schedule_time);
    }

    public static ScheduleViewHolder createView(ViewGroup parent, int viewType) {
        View view;

        Schedule.ScheduleType type = Schedule.ScheduleType.valueOf(viewType);
        if (Schedule.ScheduleType.GROUP_HEADER == type) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_header, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        }

        return new ScheduleViewHolder(view);
    }

    @Override
    public void bind(Schedule data) {
        super.bind(data);

        mSchedule = (Schedule) data;

        if (Schedule.ScheduleType.GROUP_HEADER == mSchedule.mType) {
//            mHeader.setText(mSchedule.mHeaderTitle);

        } else {
//            mDate.setText(mSchedule.mDay);
//            mContent.setText(mSchedule.mTitle);
//            mTime.setText(getDurationTime());
        }
    }

    private String getDurationTime() {
        return getTime(mSchedule.mStartDate) + " ~ " + mSchedule.mEndDate;
    }

    private static String getTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return String.format("%02d:%02d", hour, minute);
    }
}
 **/
