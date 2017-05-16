package com.vocketlist.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.MyListAdapter;
import com.vocketlist.android.api.my.MyListModel;
import com.vocketlist.android.api.my.MyListServiceManager;
import com.vocketlist.android.decoration.DividerInItemDecoration;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.roboguice.log.Ln;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 목표관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class MyListActivity extends DepthBaseActivity implements
	SwipeRefreshLayout.OnRefreshListener
	, OnMoreListener
	, RecyclerViewItemClickListener
{
	private static final String TAG = MyListActivity.class.getSimpleName();
	private static final int REQUEST_WRITE_COMMUNITY = 1000;

	@BindView(R.id.toolbar)	Toolbar toolbar;
	@BindView(R.id.recyclerView) SuperRecyclerView recyclerView;
	@BindView(R.id.ibPrevYear) AppCompatImageButton ibPrevYear;
	@BindView(R.id.tvYear) AppCompatTextView tvYear;
	@BindView(R.id.ibNextYear) AppCompatImageButton ibNextYear;
	@BindView(R.id.etContent) AppCompatEditText etContent;
	@BindView(R.id.btnDone) AppCompatTextView btnDone;

	@OnClick(R.id.ibPrevYear)
	void onPrevYearClick() {
		mCalendar.add(Calendar.YEAR, -1);

		// TODO 임시 - 나중에 서버 응답 후 OK면 갱신
		updateYear();
		reqList(mCalendar.get(Calendar.YEAR), 1);
	}

	@OnClick(R.id.ibNextYear)
	void onNextYearClick() {
		mCalendar.add(Calendar.YEAR, 1);

		// TODO 임시 - 나중에 서버 응답 후 OK면 갱신
		updateYear();
		reqList(mCalendar.get(Calendar.YEAR), 1);
	}

	@OnTextChanged(value = R.id.etContent, callback = OnTextChanged.Callback.TEXT_CHANGED)
	void onCommentTextChanged(CharSequence s, int start, int before, int count) {
		btnDone.setEnabled(s.length() > 0);
	}

	@OnClick(R.id.btnDone)
	void onDoneClick() {
		if(!TextUtils.isEmpty(etContent.getText().toString().trim())){
			reqAdd(mCalendar.get(Calendar.YEAR), etContent.getText().toString());

			if(etContent.getEditableText() != null) etContent.getEditableText().clear();
			etContent.clearFocus();
		}
		else ToastManager.show(R.string.toast_my_list_modify_empty);
	}

	private Calendar mCalendar;
	private MyListAdapter mAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_list);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		// 레이아웃 : 라사이클러
		LinearLayoutManager lm = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(lm);
		recyclerView.addItemDecoration(new DividerInItemDecoration(this, lm.getOrientation())); // 구분선
		recyclerView.setRefreshListener(this);
		recyclerView.setRefreshingColorResources(R.color.point_424C57, R.color.point_5FA9D0, R.color.material_white, R.color.point_E47B75);
		recyclerView.setupMoreListener(this, 1);

		//
		mCalendar = Calendar.getInstance(Locale.getDefault());
		updateYear();

		//
		reqList(mCalendar.get(Calendar.YEAR), 1);
	}

	@Override
	public void onRefresh() {
		recyclerView.setRefreshing(false);
		recyclerView.hideProgress();
	}

	@Override
	public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
		recyclerView.hideMoreProgress();
	}

	@Override
	public void onItemClick(View v, int position) {
		switch (v.getId()) {
			// 더보기
			case R.id.ibMore: {
				Object d = v.getTag();

				if(d instanceof MyListModel.MyList) {
					MyListModel.MyList data = (MyListModel.MyList) d;

					PopupMenu popup = new PopupMenu(v.getContext(), v, GravityCompat.END, R.attr.actionOverflowMenuStyle, 0);
					popup.inflate(R.menu.certification_modify_delete);
					popup.setOnMenuItemClickListener(item -> {
						int id = item.getItemId();
						switch (id) {
							case R.id.action_certification: doCertification(data); break;
							case R.id.action_modify: doModify(data); break;
							case R.id.action_delete: doDelete (data); break;
						}
						return true;
					});
					popup.show();
				}
			} break;

			// 기본
			default: {

			} break;
		}
	}

	/**
	 * 년도 갱신
	 */
	private void updateYear() {
		tvYear.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));
	}

	/**
	 * 인증
	 * @param data
	 */
	private void doCertification(MyListModel.MyList data) {
		Intent intent = new Intent(this, PostCUActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra(PostCUActivity.EXTRA_KEY_MYLIST_DATA, data);
		startActivityForResult(intent, REQUEST_WRITE_COMMUNITY);
	}

	/**
	 * 수정
	 * @param data
	 */
	private void doModify(MyListModel.MyList data) {
		new MaterialDialog.Builder(this)
				.title(R.string.dialog_my_list_modify_title)
				.inputType(InputType.TYPE_CLASS_TEXT)
				.input("", data.mContent, (dialog, input) -> {
					// Do something
				})
				.negativeText(R.string.cancel).onNegative((dialog, which) -> {
					dialog.dismiss();
				})
				.positiveText(R.string.modify).onPositive((dialog, which) -> {
					Log.d(TAG, "doModify: ");

					EditText et = dialog.getInputEditText();
					if(!TextUtils.isEmpty(et.getText().toString().trim())){
						dialog.dismiss();
						reqModify(data.mId, et.getText().toString());
					}
					else ToastManager.show(R.string.toast_my_list_modify_empty);
				})
				.autoDismiss(false)
				.show();
	}

	/**
	 * 삭제
	 * @param data
	 */
	private void doDelete(MyListModel.MyList data) {
		reqDelete(data.mId);
	}

	/**
	 * 요청 : 목록
	 * @param year
	 * @param page
	 */
    private void reqList(int year, int page) {
        MyListServiceManager.get(year, page)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<MyListModel>>>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						Ln.e(e, "onError");
					}

					@Override
					public void onNext(Response<BaseResponse<MyListModel>> baseResponseResponse) {
						resList(baseResponseResponse.body());
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != REQUEST_WRITE_COMMUNITY
				|| resultCode != Activity.RESULT_OK) {
			return;
		}

		reqList(mCalendar.get(Calendar.YEAR), 1);
	}

	/**
	 * 응답 : 목록
	 * @param response
	 */
	private void resList(BaseResponse<MyListModel> response) {
		List<MyListModel.MyList> myLists = response.mResult.mMyListList;
		if (myLists != null) {
			recyclerView.setAdapter(mAdapter = new MyListAdapter(myLists, this));
			mAdapter.notifyDataSetChanged();

		} else {
			recyclerView.hideProgress();
		}
	}

	/**
	 * 요청 : 추가
	 * @param content
	 */
	private void reqAdd(int year, String content) {
		showProgressDialog(true);

		MyListServiceManager.write(year, content, false)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideProgressDialog(true);
					}
				})
				.subscribe(new EmptySubscriber<Response<BaseResponse<MyListModel.MyList>>>() {
					@Override
					public void onNext(Response<BaseResponse<MyListModel.MyList>> baseResponseResponse) {
						mAdapter.add(baseResponseResponse.body().mResult);
					}
				});
	}

	/**
	 * 요청 : 삭제
	 * @param id
	 */
	private void reqDelete(int id) {
		showProgressDialog(true);

		MyListServiceManager.delete(id)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideProgressDialog(true);
					}
				})
				.subscribe(new EmptySubscriber<Response<BaseResponse<Void>>>() {
					@Override
					public void onNext(Response<BaseResponse<Void>> baseResponseResponse) {
						mAdapter.removeWithId(id);
					}
				});
	}

	/**
	 * 요청 : 수정
	 * @param id
	 * @param contents
	 */
	private void reqModify(int id, String contents) {
		showProgressDialog(true);

		MyListServiceManager.modify(id, contents, false)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideProgressDialog(true);
					}
				})
				.subscribe(new EmptySubscriber<Response<BaseResponse<MyListModel.MyList>>>() {
					@Override
					public void onNext(Response<BaseResponse<MyListModel.MyList>> baseResponseResponse) {
						mAdapter.change(baseResponseResponse.body().mResult);
					}
				});
	}
}
