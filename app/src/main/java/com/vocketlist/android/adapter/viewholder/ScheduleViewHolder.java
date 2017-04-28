package com.vocketlist.android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocketlist.android.R;
import com.vocketlist.android.dto.Schedule;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by SeungTaek.Lim on 2017. 2. 25..
 */

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
            mHeader.setText(mSchedule.mHeaderTitle);

        } else {
            mDate.setText(mSchedule.mDay);
            mContent.setText(mSchedule.mTitle);
            mTime.setText(getDurationTime());
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
