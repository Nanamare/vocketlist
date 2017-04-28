package com.vocketlist.android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.vocketlist.android.R;
import com.vocketlist.android.api.vocket.Volunteer;

import java.io.Serializable;

import butterknife.BindView;

/**
 * 뷰홀더 : 커뮤니티 : 글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerSearchViewHolder extends BaseViewHolder<Volunteer.Data> {

    @BindView(R.id.tvVolunteer) AppCompatTextView tvVolunteer;

    /**
     * 생성자
     * @param itemView
     */
    public VolunteerSearchViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Volunteer.Data data) {
        if (data instanceof Volunteer.Data) {
            Volunteer.Data volunteer = (Volunteer.Data) data;

            tvVolunteer.setText(volunteer.mTitle);
        }
    }
}
