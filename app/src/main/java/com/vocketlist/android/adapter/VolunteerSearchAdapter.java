package com.vocketlist.android.adapter;

import android.view.ViewGroup;

import com.vocketlist.android.adapter.viewholder.VolunteerSearchViewHolder;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.roboguice.log.Ln;

import java.util.List;

/**
 * 어댑터 : 봉사활동 : 검색
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerSearchAdapter extends BaseAdapter<Volunteer.Data, VolunteerSearchViewHolder> {
    private VolunteerSearchViewHolder.VolunteerSearchItemClickListener mListener;

    /**
     * 생성자
     * @param data
     */
    public VolunteerSearchAdapter(List<Volunteer.Data> data) {
        super(data);
    }

    @Override
    public VolunteerSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Ln.d("VolunteerSearchAdapter");
        VolunteerSearchViewHolder view =  VolunteerSearchViewHolder.create(parent);
        view.setListener(mListener);

        return view;
    }

    public void setListener(VolunteerSearchViewHolder.VolunteerSearchItemClickListener listener) {
        mListener = listener;
    }
}