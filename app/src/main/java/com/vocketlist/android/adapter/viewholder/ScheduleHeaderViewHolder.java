package com.vocketlist.android.adapter.viewholder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vocketlist.android.R;

/**
 * Created by SeungTaek.Lim on 2017. 2. 25..
 */

public class ScheduleHeaderViewHolder {
    private ScheduleHeaderViewHolder(View itemView) {

    }

    public static View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_header, parent, false);
    }
}
