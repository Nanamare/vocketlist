package com.vocketlist.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.api.vocket.VocketServiceManager;
import com.vocketlist.android.api.vocket.VolunteerDetail;
import com.vocketlist.android.dialog.VolunteerApplyDialog;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerReadPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 봉사활동 : 보기
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class VolunteerReadActivity extends DepthBaseActivity {
	@BindView(R.id.toolbar) protected Toolbar toolbar;
	@BindView(R.id.volunteer_read_title) protected TextView mVocketTitleText;
	@BindView(R.id.start_date_tv) protected TextView mStartDateText;
	@BindView(R.id.end_date_tv) protected TextView mEndDateText;
	@BindView(R.id.start_time_tv) protected TextView mStartTimeText;
	@BindView(R.id.end_time_tv) protected TextView mEndTimeText;
	@BindView(R.id.recruit_start_date_tv) protected TextView mRecruitStartDateText;
	@BindView(R.id.recruit_end_date_tv) protected TextView mRecruitEndDateText;
	@BindView(R.id.active_day_tv) protected TextView mActiveDayText;
	@BindView(R.id.place_tv) protected TextView mPlaceText;
	@BindView(R.id.num_by_day_tv) protected TextView mNumByDayText;
	@BindView(R.id.host_name_tv) protected TextView mHostNameText;
	@BindView(R.id.category_tv) protected TextView mCategoryText;
	@BindView(R.id.apply_btn) protected TextView mApplyBtn;
	@BindView(R.id.apply_cancel_btn) protected TextView mApplyCancelBtn;
	@BindView(R.id.write_diary_btn) protected TextView mWriteDiaryBtn;
	@BindView(R.id.isActiveDayTv) protected TextView mIsActiveDayText;
	@BindView(R.id.volunteer_read_detail_content) protected TextView mContentText;
	@BindView(R.id.volunteer_iv) protected ImageView mVolunteerImageView;

	private IVolunteerCategoryPresenter presenter;

	private IVolunteerReadPresenter volunteerReadPresenter;

	private AlertDialog dialog;

	private int vocketIndex;

	private String title;

	private BaseResponse<VolunteerDetail> mVolunteerDetail;

	private boolean isInternalApply;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volunteer_read);
		ButterKnife.bind(this);

		Intent intent = getIntent();
		if (intent != null) {
			String vocketIdx = intent.getStringExtra("vocketId");
			vocketIndex = Integer.valueOf(vocketIdx);
			System.out.print(vocketIndex);
		}

		setSupportActionBar(toolbar);

//		presenter = new VolunteerCategoryPresenter(this);
//		volunteerReadPresenter = new VolunteerReadPresenter(this);
///
//		presenter.getVocketDetail(vocketIndex);
		requestVolunteerDetail(vocketIndex);
	}

	private void requestVolunteerDetail(int vocketIndex) {
		VocketServiceManager.getVocketDetail(vocketIndex)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<VolunteerDetail>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(Response<BaseResponse<VolunteerDetail>> baseResposeResponse) {
						mVolunteerDetail = baseResposeResponse.body();
						bindVocketDetailData(mVolunteerDetail);
					}
				});
	}

	public void bindVocketDetailData(BaseResponse<VolunteerDetail> volunteerDetails) {
		mVolunteerDetail = volunteerDetails;
		mVocketTitleText.setText(volunteerDetails.mResult.mTitle);
		mStartDateText.setText(volunteerDetails.mResult.mStartDate);
		mEndDateText.setText(volunteerDetails.mResult.mEndDate);
		mActiveDayText.setText(volunteerDetails.mResult.mActiveDay);
		mPlaceText.setText(volunteerDetails.mResult.mPlace);
		mNumByDayText.setText(String.valueOf(volunteerDetails.mResult.mNumByDay));
		mHostNameText.setText(volunteerDetails.mResult.mHostName);
		mCategoryText.setText(getString(volunteerDetails.mResult.mFirstCategory.getTabResId()));
		mStartTimeText.setText(volunteerDetails.mResult.mStartTime);
		mEndTimeText.setText(volunteerDetails.mResult.mEndTime);
		mRecruitStartDateText.setText(String.valueOf(volunteerDetails.mResult.mRecruitStartDate));
		mRecruitEndDateText.setText(String.valueOf(volunteerDetails.mResult.mRecruitEndDate));
		mContentText.setText(volunteerDetails.mResult.mContent);

		if (volunteerDetails.mResult.mIsActive) {
			mIsActiveDayText.setVisibility(View.VISIBLE);
		} else {
			mIsActiveDayText.setVisibility(View.GONE);
		}
		Glide.with(this).load(getString(R.string.vocket_base_url) + volunteerDetails.mResult.mImageUrl).into(mVolunteerImageView);
		//dialog 타이틀
		title = volunteerDetails.mResult.mTitle;
		isInternalApply = volunteerDetails.mResult.mIsParticipate;

	}

	@OnClick(R.id.apply_btn)
	void apply_onClick() {
		final VolunteerApplyDialog dialog = new VolunteerApplyDialog(this, isInternalApply, mVolunteerDetail.mResult);
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dlg) {
				finish();
			}
		});

		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		dialog.show();
	}

	@OnClick(R.id.apply_cancel_btn)
	void apply_cancel_onClick() {
		Toast.makeText(this, "신청이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
		mApplyBtn.setVisibility(View.VISIBLE);
		mApplyCancelBtn.setVisibility(View.GONE);
		//todo cancel schuedule logic

//		ScheduleServiceManager.
	}
}
