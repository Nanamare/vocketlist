package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.ScheduleViewHolder;
import com.vocketlist.android.api.schedule.ScheduleListModel;

import java.util.List;

/**
 * 스케줄
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 29.
 */
public class ScheduleAdapter extends BaseAdapter<ScheduleListModel.Schedule, ScheduleViewHolder> {

    /**
     * 생성자
     *
     * @param data
     */
    public ScheduleAdapter(List<ScheduleListModel.Schedule> data) {
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