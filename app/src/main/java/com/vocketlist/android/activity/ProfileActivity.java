package com.vocketlist.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.CategoryGridAdapter;
import com.vocketlist.android.api.user.FavoritListModel;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.decoration.GridSpacingItemDecoration;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.preference.FacebookPreperence;
import com.vocketlist.android.view.LocalSelectView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 프로필관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class ProfileActivity extends DepthBaseActivity implements
		DatePickerDialog.OnDateSetListener,
		RecyclerViewItemClickListener
{
	private static final SimpleDateFormat FORMAT_SERVER = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private static final SimpleDateFormat FORMAT_LOCAL = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());

	private static final int MAX_CATEGORY = 5;

	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.civPhoto) CircleImageView civPhoto;
	@BindView(R.id.tvDisplayName) AppCompatTextView tvDisplayName;
	@BindView(R.id.tvEmail) AppCompatTextView tvEmail;

	@BindView(R.id.tvBirth) TextView tvBirth;
	@BindView(R.id.rgGender) RadioGroup rgGender;

	@BindView(R.id.rvCategory) RecyclerView rvCategory;

	@BindView(R.id.local_select_first_view) LocalSelectView localFirstView;
	@BindView(R.id.local_select_second_view) LocalSelectView localSecondView;


	@BindDimen(R.dimen.category_grid_spacing) int categoryGridSpacing;
	@BindArray(R.array.categories) String[] categories;
	@BindArray(R.array.gender) String[] genders;

	private Calendar mBirth;
	private CategoryGridAdapter mAdapter;

	@OnClick(R.id.tvForgot)
	void onForgotClick(AppCompatTextView v) {
		ToastManager.show("이메일 계정으로 로그인할 때만 사용 가능합니다.");
	}

	@OnClick(R.id.tvBirth)
	void onBirthClick(AppCompatTextView v) {
		DatePickerDialog dpd = DatePickerDialog.newInstance(
				this,
				mBirth.get(Calendar.YEAR),
				mBirth.get(Calendar.MONTH),
				mBirth.get(Calendar.DAY_OF_MONTH)
		);
		dpd.show(getSupportFragmentManager(), "Datepickerdialog");
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		mBirth.set(Calendar.YEAR, year);
		mBirth.set(Calendar.MONTH, monthOfYear);
		mBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		tvBirth.setText(FORMAT_LOCAL.format(mBirth.getTime()));
	}

	@Override
	public void onItemClick(View v, int position) {
		List<String> selectList = mAdapter.getSelectedItems();
		boolean selected = mAdapter.getSelectedItem(position);
		// 최대선택 갯수 5개 이상 선택 시
		if(selectList.size() == MAX_CATEGORY && !selected) dialogCategoryMax();
		else mAdapter.toggleSelection(position);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);

		GridLayoutManager glm = new GridLayoutManager(this, 4);
		rvCategory.setLayoutManager(glm);
		rvCategory.setAdapter(mAdapter = new CategoryGridAdapter(Arrays.asList(categories), this));
		rvCategory.addItemDecoration(new GridSpacingItemDecoration(glm.getSpanCount(), 0, categoryGridSpacing, false));
		ViewCompat.setNestedScrollingEnabled(rvCategory, false);

		//
		handleIntent();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		reqFavoriteList();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.done, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_done) doDone();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 인텐트 핸들링
	 */
	private void handleIntent() {
		initProfile();
		initProfile();
	}

	/**
	 * 프로필
	 */
	private void initProfile() {
		FacebookPreperence fp = FacebookPreperence.getInstance();

		// 사진
		if(!TextUtils.isEmpty(fp.getUserImageUrl())) {
			Glide.with(this)
					.load(fp.getUserImageUrl())
					.centerCrop()
					.crossFade()
					.into(civPhoto);
		}
		// 이름
		tvDisplayName.setText(fp.getUserName());
		// 이메일
		tvEmail.setText(fp.getEmail());

		// 생일
		String birth = FORMAT_SERVER.format(new Date());
		mBirth = Calendar.getInstance();
		mBirth.setTime(convertDateTimeFromServer(birth));
		tvBirth.setText(FORMAT_LOCAL.format(mBirth.getTime()));

		// 성별
		String gender = "남자";
		rgGender.check(gender.equals(genders[1]) ? R.id.rbMale : R.id.rbFemale);
	}

	/**
	 * 관심정보 + 지역
	 */
	private void initAddInfo() {

	}

	/**
	 * 완료
	 */
	private void doDone() {
		// 생일
		String birth = FORMAT_SERVER.format(mBirth.getTime());
		// 성별
		String gender = rgGender.getCheckedRadioButtonId() == R.id.rbFemale ? genders[0] : genders[1];

		// 관심정보
		List<String> selectList = mAdapter.getSelectedItems();
		// TODO 도 + 시군구

		// TODO 서버전송
	}

	/**
	 * 팝업 : 관심분야 최대 갯수 오버
	 */
	private void dialogCategoryMax() {
		new MaterialDialog.Builder(this)
				.content(R.string.dialog_category_max)
				.positiveText(R.string.confirm)
				.show();
	}

	private void reqChangeProfile(){

	}

	private void resChangeProfile(){

	}

	public static Date convertDateTimeFromServer(String dateString) {
		try {
			return FORMAT_SERVER.parse(dateString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return new Date();
	}

	@Override
	protected void onStop() {
		super.onStop();

		saveFavoriteList();
	}

	private void reqFavoriteList() {
		UserServiceManager.getFavorite()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new EmptySubscriber<Response<BaseResponse<FavoritListModel>>>() {
					@Override
					public void onNext(Response<BaseResponse<FavoritListModel>> baseResponseResponse) {
						FavoritListModel model = baseResponseResponse.body().mResult;

						mAdapter.clearSelections();
						for (String name : model.mFavoriteList) {
							mAdapter.setSelection(name, true);
						}
					}
				});
	}

	private void saveFavoriteList() {
		if (mAdapter == null) {
			return;
		}

		List<String> favoriteList = mAdapter.getSelectedItems();

		// todo : 0은 차후에 시군구 선택하는 다이얼로그를 통해 값을 넘겨야 함.
		UserServiceManager.setFavorite(favoriteList, null)
				.subscribe(new EmptySubscriber<Response<BaseResponse<FavoritListModel>>>());
	}
}
