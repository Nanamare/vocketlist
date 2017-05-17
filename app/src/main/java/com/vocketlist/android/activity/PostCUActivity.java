package com.vocketlist.android.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vocketlist.android.R;
import com.vocketlist.android.api.community.CommunityServiceManager;
import com.vocketlist.android.api.community.model.CommunityWrite;
import com.vocketlist.android.api.my.MyListModel;
import com.vocketlist.android.api.my.MyListServiceManager;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.common.helper.AttachmentHelper;
import com.vocketlist.android.dialog.SearchVolunteerDialog;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.helper.KeyboardHelper;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.network.error.ExceptionHelper;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.network.service.RetryPolicy;
import com.vocketlist.android.roboguice.log.Ln;
import com.vocketlist.android.view.AttachmentSingleView;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 커뮤니티 : 작성 + 수정
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class PostCUActivity extends DepthBaseActivity implements AttachmentHelper.PickerCallback {
	private static final String TAG = PostCUActivity.class.getSimpleName();

	private static final int REQUEST_WRITE_STORAGE = 112;
	public static final String EXTRA_KEY_VOLUNTEER_DATA = "EXTRA_KEY_VOLUNTEER_DATA";
	public static final String EXTRA_KEY_MYLIST_DATA = "EXTRA_KEY_MYLIST_DATA";

	@BindView(R.id.toolbar) protected Toolbar toolbar;
	@BindView(R.id.rlAttachment) RelativeLayout rlAttachment;
	@BindView(R.id.activity_post_create_mylist_content) protected AppCompatTextView mMyListContentView;
	@BindView(R.id.activity_post_create_volunteer_title) protected AppCompatTextView mAttendVolunteerView;
	@BindView(R.id.metContent) protected MaterialEditText metContent;

	@BindDimen(R.dimen.font_42) protected int searchFontSize;

	private AttachmentHelper mAttachmentHelper;
	private ChosenFile mChosenFile;

	private MyListModel.MyList mMyListData;
	private Volunteer.Data mVolunteerData;

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_WRITE_STORAGE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//reload my activity with permission granted or use the features what required the permission
				} else {
					Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
				}
			}
		}

	}

	private void checkThePemission() {
		if (Build.VERSION.SDK_INT > 22) {
			boolean hasPermission = (ContextCompat.checkSelfPermission(this,
					android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
			if (!hasPermission) {
				ActivityCompat.requestPermissions(this,
						new String[]{
								android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);
			}
		}
	}


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_post_create_update);
		ButterKnife.bind(this);

		loadExtraData(savedInstanceState);

		initView();
		checkThePemission();

		mAttachmentHelper = new AttachmentHelper(this);
		mAttachmentHelper.setPickerCallback(this);

//		handleIntent();
	}

	private void initToolbar() {
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	private void loadExtraData(Bundle savedInstanceState) {
		Bundle bundle = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();

		if (bundle == null) {
			return;
		}

		mVolunteerData = (Volunteer.Data) bundle.getSerializable(EXTRA_KEY_VOLUNTEER_DATA);
		mMyListData = (MyListModel.MyList) bundle.getSerializable(EXTRA_KEY_MYLIST_DATA);
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		outState.putSerializable(EXTRA_KEY_VOLUNTEER_DATA, mVolunteerData);
		outState.putSerializable(EXTRA_KEY_MYLIST_DATA, mMyListData);

		super.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.done, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_done) {
			requestWrite();
			return true;
		} else return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mAttachmentHelper != null)
			mAttachmentHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mAttachmentHelper != null) mAttachmentHelper.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (mAttachmentHelper != null) mAttachmentHelper.onRestoreInstanceState(savedInstanceState);
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onImagesChosen(List<ChosenImage> list) {
		initAttachment(list.get(0));
	}

	@Override
	public void onVideosChosen(List<ChosenVideo> list) {
		initAttachment(list.get(0));
	}

	@Override
	public void onFilesChosen(List<ChosenFile> list) {
		initAttachment(list.get(0));
	}

	@Override
	public void onError(String s) {
		ToastManager.show(s);
	}

	/**
	 * 봉사활동
	 */
	@OnClick(R.id.btnVolunteer)
	void onActionVolunteer() {
		SearchVolunteerDialog dialog = new SearchVolunteerDialog(this);
		dialog.setListener(new SearchVolunteerDialog.SearchDialogListener() {
			@Override
			public void onSelectedItem(Volunteer.Data data) {
				mVolunteerData = data;
				refreshAttendVolunteerView();
			}
		});
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
	}

	/**
	 * 앨범
	 */
	@OnClick(R.id.btnAlbum)
	void onActionAlbum() {
		mAttachmentHelper.doAlbumPhoto();
	}

	/**
	 * 사진촬영
	 */
	@OnClick(R.id.btnCamera)
	void onActionCamera() {
		mAttachmentHelper.doCamera();
	}

	/**
	 * 뷰 설정
	 */
	private void initView() {
		refreshAttendVolunteerView();
		refreshMyListContents();
		initToolbar();
		metContent.requestFocus();
	}

	/**
	 * 첨부파일 설정
	 *
	 * @param chosenFile
	 */
	private void initAttachment(ChosenFile chosenFile) {
		mChosenFile = chosenFile;

		final AttachmentSingleView attach = new AttachmentSingleView(this);
		rlAttachment.addView(attach);
		attach.setThumb(mChosenFile.getOriginalPath());
		attach.setOnDeleteListener(v -> {
			mChosenFile = null;
			rlAttachment.removeView(attach);
		});
	}

	private void refreshAttendVolunteerView() {
		if (mVolunteerData == null) {
			mAttendVolunteerView.setVisibility(View.GONE);
			return;
		}

		mAttendVolunteerView.setVisibility(View.VISIBLE);
		mAttendVolunteerView.setText("#" + mVolunteerData.mTitle);
	}
	private void refreshMyListContents() {
		if (mMyListData == null) {
			mMyListContentView.setVisibility(View.GONE);
			return;
		}

		mMyListContentView.setVisibility(View.VISIBLE);
		mMyListContentView.setText("#" + mMyListData.mContent);
	}

	/**
	 * 완료
	 */
	private void requestWrite() {
		if (TextUtils.isEmpty(metContent.getText().toString())) {
			Toast.makeText(this, R.string.community_write_empty_contents_message, Toast.LENGTH_SHORT).show();
			return;
		}

		String filePath = mChosenFile != null ? mChosenFile.getOriginalPath() : null;
		int serviceId = (mVolunteerData != null) ? mVolunteerData.mId : 0;

		showProgressDialog(true);
		CommunityServiceManager.write(serviceId, filePath, metContent.getText().toString())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideProgressDialog(true);
					}
				})
				.subscribe(new Subscriber<Response<BaseResponse<CommunityWrite>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						if (ExceptionHelper.isNetworkError(e)) {
							Toast.makeText(PostCUActivity.this, R.string.error_message_network, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(PostCUActivity.this, ExceptionHelper.getFirstErrorMessage(e), Toast.LENGTH_SHORT).show();
						}

						Ln.e(e, "onError : " + e.toString());
					}

					@Override
					public void onNext(Response<BaseResponse<CommunityWrite>> baseResponseResponse) {
						if (mMyListData == null) {
							Toast.makeText(PostCUActivity.this, "등록 완료", Toast.LENGTH_SHORT).show();
							setResult(Activity.RESULT_OK);
							finish();
							return;
						}

						requestMyListModify(true);
					}
				});
	}

	private void requestMyListModify(boolean isDone) {
		showProgressDialog(true);
		MyListServiceManager.modify(mMyListData.mId, mMyListData.mContent, isDone)
				.retry(new RetryPolicy())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnTerminate(new Action0() {
					@Override
					public void call() {
						hideProgressDialog(true);
					}
				})
				.subscribe(new EmptySubscriber<Response<BaseResponse<MyListModel.MyList>>>() {
					@Override
					public void onError(Throwable e) {
						if (ExceptionHelper.isNetworkError(e)) {
							Toast.makeText(PostCUActivity.this, R.string.error_message_network, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(PostCUActivity.this, ExceptionHelper.getFirstErrorMessage(e), Toast.LENGTH_SHORT).show();
						}

						Ln.e(e, "requestMyListModify > onError : " + e.toString());
					}

					@Override
					public void onNext(Response<BaseResponse<MyListModel.MyList>> baseResponseResponse) {
						Toast.makeText(PostCUActivity.this, "등록 완료", Toast.LENGTH_SHORT).show();
						setResult(Activity.RESULT_OK);
						finish();
					}
				});
	}

//	/**
//	 * 다이얼로그 : 페이스북 공유
//	 */
//	private void shareToFacebook() {
//		ShareLinkContent content = new ShareLinkContent.Builder()
//				//링크의 콘텐츠 제목
//				.setContentTitle(FacebookPreperence.getInstance().getUserName() + "님이 작성하신 글입니다.")
//				//게시물에 표시될 썸네일 이미지의 URL
//				.setImageUrl(Uri.parse("https://i.vimeocdn.com/video/620559869_1280.jpg"))
//				//공유될 링크 (봉사 활동에 대한 내용)
//				.setContentUrl(Uri.parse("http://52.78.106.73:8080/kozy/"))
//				//게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
//				.setContentDescription(metContent.getText().toString())
//				.build();
//
//		ShareDialog shareDialog = new ShareDialog(this);
//		shareDialog.show(content, ShareDialog.Mode.FEED);
//	}
}
