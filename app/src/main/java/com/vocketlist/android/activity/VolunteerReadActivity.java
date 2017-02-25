package com.vocketlist.android.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vocketlist.android.R;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.presenter.IView.IVolunteerReadView;
import com.vocketlist.android.presenter.VolunteerCategoryPresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;
import com.vocketlist.android.util.SharePrefUtil;

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

	private IVolunteerCategoryPresenter presenter;

	private AlertDialog dialog;

	private int voketIndex;

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
		start_time_tv.setText(String.valueOf(volunteerDetails.mResult.mStartTime));
		end_time_tv.setText(String.valueOf(volunteerDetails.mResult.mEndTime));
		recruit_start_date_tv.setText(String.valueOf(volunteerDetails.mResult.mRecruitStartDate));
		recruit_end_date_tv.setText(String.valueOf(volunteerDetails.mResult.mRecruitEndDate));
		if (volunteerDetails.mResult.mIsActive) {
			isActiveDayTv.setVisibility(View.VISIBLE);
		} else {
			isActiveDayTv.setVisibility(View.GONE);
		}

	}

	@OnClick(R.id.apply_btn)
	void apply_btn_click() {
		final View innerView = getLayoutInflater().inflate(R.layout.dialog_voket_apply, null);
		EditText name = (EditText) innerView.findViewById(R.id.dialog_apply_name_edt);
		EditText email = (EditText) innerView.findViewById(R.id.dialog_apply_email_edt);
		EditText phone = (EditText) innerView.findViewById(R.id.dialog_apply_phone_edt);
		Button doneBtn = (Button) innerView.findViewById(R.id.dialog_apply_done_btn);
		Button cancelBtn = (Button) innerView.findViewById(R.id.dialog_apply_cancel_btn);
		AlertDialog.Builder alert = new AlertDialog.Builder(VolunteerReadActivity.this);
		alert.setView(innerView);

		doneBtn.setOnClickListener(view -> {
			if (isValid(name.getText().toString(), email.getText().toString(), phone.getText().toString())) {
				//todo
				addScheduleNoti(view);
				apply_btn.setVisibility(View.GONE);
				apply_cancel_btn.setVisibility(View.VISIBLE);
				dialog.dismiss();
			}

		});

		cancelBtn.setOnClickListener(view -> {
			dialog.dismiss();
		});

		dialog = alert.create();
		dialog.show();

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

		String expression = "(01[016789])-(\\d{3,4})-(\\d{4})";

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

	private void addScheduleNoti(View view) {
//		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
//		Notification.Builder mBuilder = new Notification.Builder(this);
//		mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//		mBuilder.setTicker("글로벌지식교류 NGO 편집 및 디자인 작업" + " 이 스케줄관리에 추가되었습니다.");
//		mBuilder.setWhen(System.currentTimeMillis());
//		mBuilder.setContentTitle("TEST");
//		mBuilder.setContentText("TEST");
//		mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//		mBuilder.setContentIntent(pendingIntent);
//		mBuilder.setAutoCancel(true);
//
//		nm.notify(333, mBuilder.build());


//		LayoutInflater inflater = getLayoutInflater();
//		View layout = inflater.inflate(R.layout.toast_notifacation, (ViewGroup) findViewById(R.id.toast_root_ll));
//		TextView textView = (TextView) layout.findViewById(R.id.toast_noti_title);
//		textView.setText(volunteerDetail.mResult.mTitle);
//		Toast toast = new Toast(getApplicationContext());
//		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.TOP, 0, 0);
//		toast.setDuration(Toast.LENGTH_LONG);
//		toast.setView(layout);
//		toast.show();



	}

	@OnClick(R.id.write_diary_btn)
	void apply_btn_onClick() {


	}

}
