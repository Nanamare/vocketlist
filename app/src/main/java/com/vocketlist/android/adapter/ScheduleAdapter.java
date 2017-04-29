package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.ScheduleViewHolder;
import com.vocketlist.android.dto.Schedule;

import java.util.List;

/**
 * 스케줄
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 29.
 */
public class ScheduleAdapter extends BaseAdapter<Schedule, ScheduleViewHolder> {

    /**
     * 생성자
     *
     * @param data
     */
    public ScheduleAdapter(List<Schedule> data) {
        super(data);
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}

/**
public class ScheduleAdapter extends BaseAdapter<Schedule, ScheduleViewHolder> {

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
}**/
