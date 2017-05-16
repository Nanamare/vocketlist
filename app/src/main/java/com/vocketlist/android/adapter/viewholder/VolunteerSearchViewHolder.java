package com.vocketlist.android.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.api.vocket.Volunteer;

import butterknife.BindView;

/**
 * 뷰홀더 : 커뮤니티 : 글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerSearchViewHolder extends BaseViewHolder<Volunteer.Data> {

    @BindView(R.id.tvVolunteer) AppCompatTextView tvVolunteer;

    private VolunteerSearchItemClickListener mListener;

    public static VolunteerSearchViewHolder create(ViewGroup parent) {
        return new VolunteerSearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer_search, parent, false));
    }

    /**
     * 생성자
     * @param itemView
     */
    private VolunteerSearchViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null) {
                    mListener.onClickVolunteerItem(data);
                }
            }
        });
    }

    @Override
    public void bind(Volunteer.Data data) {
        super.bind(data);

        tvVolunteer.setText(data.mTitle);
    }

    public void setListener(VolunteerSearchItemClickListener listener) {
        mListener = listener;
    }

    public interface VolunteerSearchItemClickListener {
        void onClickVolunteerItem(Volunteer.Data data);
    }
}
