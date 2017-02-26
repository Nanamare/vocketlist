package com.vocketlist.android.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.presenter.IView.IVolunteerReadView;
import com.vocketlist.android.presenter.VolunteerCategoryPresenter;
import com.vocketlist.android.presenter.VolunteerReadPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerReadPresenter;
import com.vocketlist.android.util.SharePrefUtil;

import org.w3c.dom.Text;

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
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.voket_title_tv)
	TextView voket_title_tv;
	@BindView(R.id.start_date_tv)
	TextView start_date_tv;
	@BindView(R.id.end_date_tv)
	TextView end_date_tv;
	@BindView(R.id.start_time_tv)
	TextView start_time_tv;
	@BindView(R.id.end_time_tv)
	TextView end_time_tv;
	@BindView(R.id.recruit_start_date_tv)
	TextView recruit_start_date_tv;
	@BindView(R.id.recruit_end_date_tv)
	TextView recruit_end_date_tv;
	@BindView(R.id.active_day_tv)
	TextView active_day_tv;
	@BindView(R.id.place_tv)
	TextView place_tv;
	@BindView(R.id.num_by_day_tv)
	TextView num_by_day_tv;
	@BindView(R.id.host_name_tv)
	TextView host_name_tv;
	@BindView(R.id.category_tv)
	TextView category_tv;
	@BindView(R.id.apply_btn)
	Button apply_btn;
	@BindView(R.id.apply_cancel_btn)
	Button apply_cancel_btn;
	@BindView(R.id.write_diary_btn)
	Button write_diary_btn;
	@BindView(R.id.isActiveDayTv)
	TextView isActiveDayTv;
	@BindView(R.id.content_tv)
	TextView content_tv;
	@BindView(R.id.volunteer_iv)
	ImageView volunteer_iv;

	private IVolunteerCategoryPresenter presenter;

	private IVolunteerReadPresenter volunteerReadPresenter;

	private AlertDialog dialog;

	private int voketIndex;

	private String title;

	private BaseResponse<VolunteerDetail> volunteerDetail;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volunteer_read);
		ButterKnife.bind(this);

		Intent intent = getIntent();
		if (intent != null) {
			String voketIdx = intent.getStringExtra("voketId");
			voketIndex = Integer.valueOf(voketIdx);
			System.out.print(voketIndex);
		}

		setSupportActionBar(toolbar);

		presenter = new VolunteerCategoryPresenter(this);
		volunteerReadPresenter = new VolunteerReadPresenter(this);

		String token = SharePrefUtil.getSharedPreference("token");
		presenter.getVoketDetail(voketIndex);
	}

	@Override
	public void bindVoketDetailData(BaseResponse<VolunteerDetail> volunteerDetails) {
		volunteerDetail = volunteerDetails;
		voket_title_tv.setText(volunteerDetails.mResult.mTitle);
		start_date_tv.setText(volunteerDetails.mResult.mStartDate);
		end_date_tv.setText(volunteerDetails.mResult.mEndDate);
		active_day_tv.setText(volunteerDetails.mResult.mActiveDay);
		place_tv.setText(volunteerDetails.mResult.mPlace);
		num_by_day_tv.setText(String.valueOf(volunteerDetails.mResult.mNumByDay));
		host_name_tv.setText(volunteerDetails.mResult.mHostName);
		category_tv.setText(volunteerDetails.mResult.mFirstCategory);
		start_time_tv.setText(volunteerDetails.mResult.mStartTime);
		end_time_tv.setText(volunteerDetails.mResult.mEndTime);
		recruit_start_date_tv.setText(String.valueOf(volunteerDetails.mResult.mRecruitStartDate));
		recruit_end_date_tv.setText(String.valueOf(volunteerDetails.mResult.mRecruitEndDate));
		content_tv.setText(volunteerDetails.mResult.mContent);
		if (volunteerDetails.mResult.mIsActive) {
			isActiveDayTv.setVisibility(View.VISIBLE);
		} else {
			isActiveDayTv.setVisibility(View.GONE);
		}
		Glide.with(this).load("http://www.vocketlist.com"+volunteerDetails.mResult.mImageUrl).into(volunteer_iv);
		//dialog 타이틀
		title = volunteerDetails.mResult.mTitle;

	}

	@OnClick(R.id.apply_btn)
	void apply_onClick(){
		View innerView = getLayoutInflater().inflate(R.layout.dialog_voket_apply, null);
		EditText name = (EditText) innerView.findViewById(R.id.dialog_apply_name_edt);
		EditText email = (EditText) innerView.findViewById(R.id.dialog_apply_email_edt);
		EditText phone = (EditText) innerView.findViewById(R.id.dialog_apply_phone_edt);
		Button doneBtn = (Button) innerView.findViewById(R.id.dialog_apply_done_btn);
		Button cancelBtn = (Button) innerView.findViewById(R.id.dialog_apply_cancel_btn);
		TextView textView = (TextView)innerView.findViewById(R.id.dialog_vocket_title);
		AlertDialog.Builder alert = new AlertDialog.Builder(VolunteerReadActivity.this);
		alert.setView(innerView);

		textView.setText(title);

		doneBtn.setOnClickListener(v -> {
			if (isValid(name.getText().toString(), email.getText().toString(), phone.getText().toString())) {
				//todo
				apply_btn.setVisibility(View.GONE);
				apply_cancel_btn.setVisibility(View.VISIBLE);
				dialog.dismiss();

				//presenter 자리
				volunteerReadPresenter.applyVolunteer(name.getText().toString(), email.getText().toString(), voketIndex);
			}

		});

		cancelBtn.setOnClickListener(v -> {
			dialog.dismiss();
		});

		dialog = alert.create();
		dialog.show();
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
		apply_btn.setVisibility(View.VISIBLE);
		apply_cancel_btn.setVisibility(View.GONE);
		//todo cancel schuedule logic
	}


}
