package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.GridViewAdapter;
import com.vocketlist.android.util.TimePickerDialog;
import com.vocketlist.android.view.NonNestScrollGridView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 프로필관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class ProfileActivity extends DepthBaseActivity {

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.profileGv)
	NonNestScrollGridView gridView;
	@BindView(R.id.profile_birth_tv)
	TextView profile_birth_tv;
	@BindView(R.id.woman_radioBtn)
	AppCompatRadioButton woman_radioBtn;
	@BindView(R.id.man_radioBtn)
	AppCompatRadioButton man_radioBtn;

	private GridViewAdapter gridViewAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);

		gridViewAdapter = new GridViewAdapter(getApplicationContext());
		gridView.setAdapter(gridViewAdapter);
		gridView.setNonScroll(true);


	}

	@Override
	protected void onStart() {
		super.onStart();

		profile_birth_tv.setOnClickListener(view -> {
			TimePickerDialog dialog = TimePickerDialog.newInstance(view);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			dialog.show(ft, "TimeDialog");

		});
	}

}
