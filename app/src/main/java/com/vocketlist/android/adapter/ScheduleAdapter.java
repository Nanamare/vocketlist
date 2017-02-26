package com.vocketlist.android.adapter;

import android.view.ViewGroup;

import com.vocketlist.android.adapter.viewholder.ScheduleViewHolder;
import com.vocketlist.android.dto.Schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 2. 25..
 */

public class ScheduleAdapter extends BaseAdapter<ScheduleViewHolder> {
    public ScheduleAdapter(List<Schedule> data) {
        super(data);
    }

    @Override
    public int getItemViewType(int position) {
        Schedule schedule = getItem(position);
        return schedule.mType.getValue();
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ScheduleViewHolder.createView(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        int prePosition = position - 1;
        Schedule preSchedule;
        Schedule schedule = getItem(position);

        if (schedule.mStartDate != null) {
            schedule.mDay = String.format("%d 일", getDayOfMonth(schedule.mStartDate));
        }

        if (prePosition >= 0 && schedule.mStartDate != null) {
            preSchedule = getItem(prePosition);
            int day = getDayOfMonth(schedule.mStartDate);
            int preDay = getDayOfMonth(preSchedule.mStartDate);

            if (day == preDay) {
                schedule.mDay = null;
            }
        }

        holder.bind(schedule);
    }

    private int getDayOfMonth(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int wk = calendar.get(Calendar.DAY_OF_WEEK);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
