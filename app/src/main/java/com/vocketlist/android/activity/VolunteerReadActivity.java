package com.vocketlist.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.VolunteerDetail;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.presenter.IView.IVolunteerReadView;
import com.vocketlist.android.presenter.VolunteerCategoryPresenter;
import com.vocketlist.android.presenter.VolunteerReadPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerReadPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 봉사활동 : 보기
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class VolunteerReadActivity extends DepthBaseActivity implements IVolunteerReadView {
	@BindView(R.id.toolbar) protected Toolbar toolbar;
	@BindView(R.id.vocket_title_tv) protected TextView mVocketTitleText;
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
	@BindView(R.id.apply_btn) protected Button mApplyBtn;
	@BindView(R.id.apply_cancel_btn) protected Button mApplyCancelBtn;
	@BindView(R.id.write_diary_btn) protected Button mWriteDiaryBtn;
	@BindView(R.id.isActiveDayTv) protected TextView mIsActiveDayText;
	@BindView(R.id.content_tv) protected TextView mContentText;
	@BindView(R.id.volunteer_iv) protected ImageView mVolunteerImageView;

	private IVolunteerCategoryPresenter presenter;

	private IVolunteerReadPresenter volunteerReadPresenter;

	private AlertDialog dialog;

	private int vocketIndex;

	private String title;

	private BaseResponse<VolunteerDetail> volunteerDetail;

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

		presenter = new VolunteerCategoryPresenter(this);
		volunteerReadPresenter = new VolunteerReadPresenter(this);

		presenter.getVocketDetail(vocketIndex);
	}

	@Override
	public void bindVocketDetailData(BaseResponse<VolunteerDetail> volunteerDetails) {
		volunteerDetail = volunteerDetails;
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
		Glide.with(this).load("http://www.vocketlist.com" + volunteerDetails.mResult.mImageUrl).into(mVolunteerImageView);
		//dialog 타이틀
		title = volunteerDetails.mResult.mTitle;
		isInternalApply = volunteerDetails.mResult.mIsParticipate;

	}

	@OnClick(R.id.apply_btn)
	void apply_onClick() {
		if (isInternalApply == true) {
			View innerView = getLayoutInflater().inflate(R.layout.dialog_vocket_apply, null);
			EditText name = (EditText) innerView.findViewById(R.id.dialog_apply_name_edt);
			EditText email = (EditText) innerView.findViewById(R.id.dialog_apply_email_edt);
			EditText phone = (EditText) innerView.findViewById(R.id.dialog_apply_phone_edt);
			Button doneBtn = (Button) innerView.findViewById(R.id.dialog_apply_done_btn);
			Button cancelBtn = (Button) innerView.findViewById(R.id.dialog_apply_cancel_btn);
			TextView textView = (TextView) innerView.findViewById(R.id.dialog_vocket_title);
			AlertDialog.Builder alert = new AlertDialog.Builder(VolunteerReadActivity.this);
			alert.setView(innerView);

			textView.setText(title);

			doneBtn.setOnClickListener(v -> {
				if (isValid(name.getText().toString(), email.getText().toString(), phone.getText().toString())) {
					//todo
					mApplyBtn.setVisibility(View.GONE);
					mApplyCancelBtn.setVisibility(View.VISIBLE);
					dialog.dismiss();

					//presenter 자리
					volunteerReadPresenter.applyVolunteer(name.getText().toString(), email.getText().toString(), vocketIndex);
				}

			});

			cancelBtn.setOnClickListener(v -> {
				dialog.dismiss();
			});
			dialog = alert.create();
			dialog.show();
		} else {
			View innerView = getLayoutInflater().inflate(R.layout.dialog_vocket_internal_apply, null);
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setView(innerView);
			dialog = alert.create();
			dialog.show();

		}
	}

	@Override
	public void showCompleteDialog() {

		ToastManager.showAddSchedule(this, "서버 등록 완료&&스케줄 등록 완료");

	}


	private boolean isValid(String name, String email, String phoneNumber) {

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(getApplicationContext(), "이름을 적어주세요",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (name.matches("[^가-힣A-Za-z ]")) {
			Toast.makeText(getApplicationContext(), "이름 형식을 맞춰주세요",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!isEmailValid(email)) {
			Toast.makeText(getApplicationContext(), "이메일 형식을 맞춰주세요",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!isPhoneValid(phoneNumber)) {
			Toast.makeText(getApplicationContext(), "전화번호 형식을 맞춰주세요",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private boolean isPhoneValid(String phoneNumber) {
		boolean isValid = false;

		String expression = "(01[016789])(\\d{3,4})(\\d{4})";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(phoneNumber);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;

	}


	private boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	@OnClick(R.id.apply_cancel_btn)
	void apply_cancel_onClick() {
		Toast.makeText(this, "신청이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
		mApplyBtn.setVisibility(View.VISIBLE);
		mApplyCancelBtn.setVisibility(View.GONE);
		//todo cancel schuedule logic
	}


}
