package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.vocketlist.android.R;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.presenter.IView.IVolunteerReadView;
import com.vocketlist.android.presenter.VolunteerCategoryPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;
import com.vocketlist.android.util.SharePrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 봉사활동 : 보기
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class VolunteerReadActivity extends DepthBaseActivity implements IVolunteerReadView {
	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.voket_title_tv) TextView voket_title_tv;
	@BindView(R.id.start_date_tv) TextView start_date_tv;
	@BindView(R.id.end_date_tv) TextView end_date_tv;
	@BindView(R.id.start_time_tv) TextView start_time_tv;
	@BindView(R.id.end_time_tv) TextView end_time_tv;
	@BindView(R.id.recruit_start_date_tv) TextView recruit_start_date_tv;
	@BindView(R.id.recruit_end_date_tv) TextView recruit_end_date_tv;
	@BindView(R.id.active_day_tv) TextView active_day_tv;
	@BindView(R.id.place_tv) TextView place_tv;
	@BindView(R.id.num_by_day_tv) TextView num_by_day_tv;
	@BindView(R.id.host_name_tv) TextView host_name_tv;
	@BindView(R.id.category_tv) TextView category_tv;
	@BindView(R.id.apply_btn) Button apply_btn;
	@BindView(R.id.write_diary_btn) Button write_diary_btn;

	private IVolunteerCategoryPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volunteer_read);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		presenter = new VolunteerCategoryPresenter(this);

		String token = SharePrefUtil.getSharedPreference("token");
		presenter.getVoketDetail(token);
	}

	@Override
	public void bindVoketDetailData(VolunteerDetail volunteerDetails) {
		voket_title_tv.setText(volunteerDetails.getTitle());
		start_date_tv.setText(volunteerDetails.getStart_date());
		end_date_tv.setText(volunteerDetails.getEnd_date());
		start_time_tv.setText(volunteerDetails.getStart_time());
		end_time_tv.setText(volunteerDetails.getEnd_date());
		recruit_start_date_tv.setText(volunteerDetails.getRecruit_start_date());
		recruit_end_date_tv.setText(volunteerDetails.getRecruit_end_date());
		active_day_tv.setText(volunteerDetails.getActiveDay());
		place_tv.setText(volunteerDetails.getPlace());
		num_by_day_tv.setText(volunteerDetails.getNum_by_day());
		host_name_tv.setText(volunteerDetails.getHostName());
		category_tv.setText(volunteerDetails.getFirst_category());

	}
}
