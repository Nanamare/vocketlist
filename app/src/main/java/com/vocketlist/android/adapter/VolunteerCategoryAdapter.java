package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.VolunteerCategoryViewHolder;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Volunteer;

import java.util.List;

/**
 * 어댑터 : 봉사활동
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerCategoryAdapter extends BaseAdapter<VolunteerCategoryViewHolder> {
    /**
     * 생성자
     * @param volunteerList
     */
    public VolunteerCategoryAdapter(List<Volunteer.Data> volunteerList) {
        super(volunteerList);
    }

    @Override
    public VolunteerCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VolunteerCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer, parent, false));
    }

}
