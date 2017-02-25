package com.vocketlist.android.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.VolunteerSearchAdapter;
import com.vocketlist.android.defined.Extras;
import com.vocketlist.android.dto.MyList;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.helper.AttachmentHelper;
import com.vocketlist.android.manager.ToastManager;
import com.vocketlist.android.net.Parameters;
import com.vocketlist.android.view.AttachmentSingleView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 커뮤니티 : 작성 + 수정
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class PostCUActivity extends DepthBaseActivity implements AttachmentHelper.PickerCallback {
	private static final String TAG = PostCUActivity.class.getSimpleName();

	private static final int REQUEST_WRITE_STORAGE = 112;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int RUQUEST_IMAGE_FROM_ALBUM = 5;

	@BindView(R.id.toolbar)
	Toolbar toolbar;
//	@BindView(R.id.activity_post_create_update_picTv)
//	TextView picTv;
//	@BindView(R.id.activity_post_create_update_picIv)
//	ImageView picIv;
//	@BindView(R.id.activity_post_create_update_shareToFb_tv)
//	TextView shareToFb_tv;

//	private Bitmap bp;
//	private File imgfile;
//	private String mCurrentPhotoPath;

	@BindView(R.id.rlAttachment) RelativeLayout rlAttachment;
	@BindView(R.id.metVolunteer) MaterialEditText metVolunteer;
	@BindView(R.id.metMyList) MaterialEditText metMyList;
	@BindView(R.id.metContent) MaterialEditText metContent;

	@BindDimen(R.dimen.font_42) int searchFontSize;

	/**
	 * 봉사활동
	 */
	@OnClick(R.id.btnVolunteer)
	void onActionVolunteer() {
		dialogVolunteer();
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
	 * 액션 : 페이스북 공유
	 */
	@OnClick(R.id.btnFacebook)
	void onActionFacebook() {
		shareToFacebook();
	}

	/**
	 * 봉사활동 검색어 수신
	 */
	private SearchView.OnQueryTextListener onSearchQueryTextListener = new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String query) {
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			// 실시간으로 처리하지 말고 재입력 시간 Delay 후에 서버에 쿼리 요청
			if(mSearchQueryRunnable != null) searchQueryHandler.removeCallbacks(mSearchQueryRunnable);
			searchQueryHandler.postDelayed(
					mSearchQueryRunnable = new SearchQueryRunnable(newText),
					300
			);

			return false;
		}
	};

	/**
	 * 핸들러 : 검색
	 */
	private Handler searchQueryHandler = new Handler();

	/**
	 * 실행자 : 검색
	 */
	private class SearchQueryRunnable implements Runnable {
		private String query;

		/**
		 * 생성자
		 * @param query
		 */
		public SearchQueryRunnable(String query) {
			this.query = query;
		}

		@Override
		public void run() {
			reqVolunteers(query);
		}
	}
	private SearchQueryRunnable mSearchQueryRunnable;

	private AttachmentHelper mAttachmentHelper;
	private ChosenFile mChosenFile;
	private Volunteer.Data mVolunteer;
	private MyList.Data mMyList;

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
		setSupportActionBar(toolbar);

//		// 헤더 CI 적용
//		getSupportActionBar().setDisplayShowCustomEnabled(true);
//		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		getSupportActionBar().setCustomView(
//				getLayoutInflater().inflate(R.layout.appbar_sub_title, null),
//				new ActionBar.LayoutParams(
//						ActionBar.LayoutParams.WRAP_CONTENT,
//						ActionBar.LayoutParams.WRAP_CONTENT,
//						Gravity.CENTER
//				)
//		);

		checkThePemission();

//		picTv.setOnClickListener(view -> takepicture());
//		shareToFb_tv.setOnClickListener(view -> shareToFacebook());

		//
		mAttachmentHelper = new AttachmentHelper(this);
		mAttachmentHelper.setPickerCallback(this);

		//
		handleIntent();
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
		int id = item.getItemId();
		if(id == R.id.action_done) {
			doDone();
			return true;
		}
		else return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(mAttachmentHelper != null) mAttachmentHelper.onActivityResult(requestCode, resultCode, data);
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
	 * 인덴트 : 핸들링
	 */
	private void handleIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if(extras != null) {
				Serializable v = extras.getSerializable(Extras.VOLUNTEER);
				Serializable m = extras.getSerializable(Extras.MY_LIST);

				// 봉사활동 -> 후기작성
				if(v != null && v instanceof Volunteer.Data) initVolunteer((Volunteer.Data) v);
				// 마이리스트 -> 인증하기
				else if(m != null && m instanceof MyList.Data) initMyList((MyList.Data) m);
				// 커뮤니티 -> 글작성
				else {}

				//
				initView();
			}
		}
	}

	/**
	 * 뷰 설정
	 */
	private void initView() {
		//
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

	/**
	 * TODO 봉사활동 설정
	 */
	private void initVolunteer(Volunteer.Data v) {
		mVolunteer = v;
		metVolunteer.setVisibility(View.VISIBLE);
	}

	/**
	 * TODO 마이리스트 설정
	 */
	private void initMyList(MyList.Data m) {
		mMyList = m;
		metMyList.setVisibility(View.VISIBLE);
	}

	/**
	 * 완료
	 * TODO validation
	 */
	private void doDone() {
	}

	/**
	 * 다이얼로그 : 봉사활동 목록
	 */
	private void dialogVolunteer() {
		MaterialDialog md = new MaterialDialog.Builder(this)
				.customView(R.layout.dialog_search, false)
				.show();
		View cv = md.getCustomView();
		SearchView sv = ButterKnife.findById(md, R.id.svVolunteer);
		SuperRecyclerView rv = ButterKnife.findById(md, R.id.recyclerView);

		// 검색뷰 폰트 크기
		SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) sv.findViewById(R.id.search_src_text);
		theTextArea.setTextSize(TypedValue.COMPLEX_UNIT_PX, searchFontSize);
		theTextArea.requestFocus();
		sv.requestFocus();
		sv.setOnQueryTextListener(onSearchQueryTextListener);

		// 더미
		List<Volunteer.Data> dummy = new ArrayList<>();
		for (int i = 0; i < 20; i++) dummy.add(new Volunteer.Data());

		// 데이터 설정
		rv.setAdapter(new VolunteerSearchAdapter(dummy));
	}

	/**
	 * 다이얼로그 : 페이스북 공유
	 */
	private void shareToFacebook() {
		ShareLinkContent content = new ShareLinkContent.Builder()
				//링크의 콘텐츠 제목
				.setContentTitle("봉사활동 후기")
				//게시물에 표시될 썸네일 이미지의 URL
				.setImageUrl(Uri.parse("https://4310b1a9-a-5b13c88f-s-sites.googlegroups.com/a/j2edu.co.kr/home/bongsa-hwaldong/%EB%B4%89%EC%82%AC.jpg?attachauth=ANoY7crzTtirlQHaUHt2tEZ7WSgQn_Tws7PC3oHFMh-kRkg64THIgwKT5wYar1sbt-aNqWWb5hCZnQvAm3mxppJFpXZoHhfwUoERcyyiVXuEWYnKeLaawhd22lVdSRcKwhAiKS5CfN7Sy1WOhDFEsLJQPJSW-RD_xgNgo_Ny2NbCTGeCqUkroOoqt0oRCZbAWyLP7vkr2E9UZXW9USgy0psElpPqa3lNrbOw_nGxbhPKaFtuDTIeF6o%3D&attredirects=0"))
				//공유될 링크
				.setContentUrl(Uri.parse("http://52.78.106.73:8080/kozy/"))
				//게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
				.setContentDescription("지난 금요일 봉사활동 다녀온 후기입니다 ^^")
				.build();

		ShareDialog shareDialog = new ShareDialog(this);
		shareDialog.show(content, ShareDialog.Mode.FEED);
	}

	/**
	 * TODO 요청 : 스케줄에 등록된 봉사활동 목록
	 */
	private void reqMyVolunteers() {

	}

	/**
	 * TODO 요청 : 봉사활동 검색
	 */
	private void reqVolunteers(String query) {
		Log.d(TAG, "reqVolunteers: query : " + query);
	}

	/**
	 * 요청 : 글작성
	 */
	private void reqPost() {
		Map<String, RequestBody> fields = new HashMap<>();

		String content = metContent.getText().toString();

		// 유효성 체크
		if(TextUtils.isEmpty(content.trim()) && mChosenFile == null) {
			ToastManager.show(R.string.toast_invalid_content);
		}

		// 내용
		fields.put(Parameters.CONTENT, RequestBody.create(okhttp3.MultipartBody.FORM, content));
		// 첨부파일
		if(mChosenFile != null) {
			File file = new File(mChosenFile.getOriginalPath());
			RequestBody requestFile = RequestBody.create(MediaType.parse(mChosenFile.getMimeType()), file);
			fields.put(Parameters.ATTACH + "\"; filename=\"" + file.getName(), requestFile);
		}
		// 봉사활동
		if(mVolunteer != null) fields.put(Parameters.VOLUNTEER_ID, RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(mVolunteer.mId)));
		// 마이리스트
		if(mMyList != null) fields.put(Parameters.MY_LIST_ID, RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(mMyList.id)));

		// TODO 서버요청
	}

	/**
	private void shareToFacebook() {
		ShareLinkContent content = new ShareLinkContent.Builder()
				//링크의 콘텐츠 제목
				.setContentTitle("봉사활동 후기")
				//게시물에 표시될 썸네일 이미지의 URL
				.setImageUrl(Uri.parse("https://4310b1a9-a-5b13c88f-s-sites.googlegroups.com/a/j2edu.co.kr/home/bongsa-hwaldong/%EB%B4%89%EC%82%AC.jpg?attachauth=ANoY7crzTtirlQHaUHt2tEZ7WSgQn_Tws7PC3oHFMh-kRkg64THIgwKT5wYar1sbt-aNqWWb5hCZnQvAm3mxppJFpXZoHhfwUoERcyyiVXuEWYnKeLaawhd22lVdSRcKwhAiKS5CfN7Sy1WOhDFEsLJQPJSW-RD_xgNgo_Ny2NbCTGeCqUkroOoqt0oRCZbAWyLP7vkr2E9UZXW9USgy0psElpPqa3lNrbOw_nGxbhPKaFtuDTIeF6o%3D&attredirects=0"))
				//공유될 링크
				.setContentUrl(Uri.parse("http://52.78.106.73:8080/kozy/"))
				//게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
				.setContentDescription("지난 금요일 봉사활동 다녀온 후기입니다 ^^")
				.build();

		ShareDialog shareDialog = new ShareDialog(this);
		shareDialog.show(content, ShareDialog.Mode.FEED);


	}

	public void takepicture() {
		final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(PostCUActivity.this);
		builder.setTitle("Choose Option")
				.setItems(items, (dialog, index) -> {
					if (index == 0) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (intent.resolveActivity(getPackageManager()) != null) {
							// Create the File where the photo should go
							imgfile = null;
							try {
								imgfile = createImageFile();
							} catch (IOException ex) {
								// Error occurred while creating the File
								ex.printStackTrace();
							}
							// Continue only if the File was successfully created
							if (imgfile != null) {
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgfile));
								startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
								picTv.setVisibility(View.GONE);
							}
						}
					} else if (index == 1) {
						imgfile = null;
						try {
							imgfile = createImageFile();
						} catch (IOException ex) {
							// Error occurred while creating the File
							ex.printStackTrace();
						}
						if (imgfile != null) {
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
							startActivityForResult(intent, RUQUEST_IMAGE_FROM_ALBUM);
							picTv.setVisibility(View.GONE);
						}
					} else {
						Toast.makeText(getApplicationContext(), items[index], Toast.LENGTH_SHORT).show();
					}
				});


		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  // prefix
				".jpg",         // suffix
				storageDir      // directory
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case RUQUEST_IMAGE_FROM_ALBUM: {
				if (resultCode == Activity.RESULT_OK) {
					Uri imageUri = data.getData();
					try {
						bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
					} catch (IOException e) {
						e.printStackTrace();
					}

					SaveBitmapToFileCache(bp, imgfile.getAbsolutePath());
					picIv.setImageBitmap(resizeBitmap(bp, 2048));

					break;
				} else {
					finish();
				}
			}
			case REQUEST_IMAGE_CAPTURE: {
				if (resultCode == Activity.RESULT_OK) {

					try {
						bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
					} catch (IOException e) {
						e.printStackTrace();
					}

					Matrix matrix = new Matrix();
					ExifInterface ei = null;
					try {
						ei = new ExifInterface(imgfile.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}

					int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);

					switch (orientation) {
						case 1:
							break;
						case 6:
							matrix.postRotate(90);
							break;
						case 8:
							matrix.postRotate(-90);
							break;
						default:
							matrix.postRotate(90);
							break;
					}

					if (bp != null) {
						Bitmap rotatedBitmap = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
						picIv.setImageBitmap(resizeBitmap(rotatedBitmap, 2048));
						SaveBitmapToFileCache(bp, imgfile.getAbsolutePath());

					}
				} else {
					finish();
				}
			}
		}

	}

	private Bitmap resizeBitmap(Bitmap src, int maxRes) {
		int iWidth = src.getWidth();      //비트맵이미지의 넓이
		int iHeight = src.getHeight();     //비트맵이미지의 높이
		int newWidth = iWidth;
		int newHeight = iHeight;
		float rate = 0.0f;

		//이미지의 가로 세로 비율에 맞게 조절
		if (iWidth > iHeight) {
			if (maxRes < iWidth) {
				rate = maxRes / (float) iWidth;
				newHeight = (int) (iHeight * rate);
				newWidth = maxRes;
			}
		} else {
			if (maxRes < iHeight) {
				rate = maxRes / (float) iHeight;
				newWidth = (int) (iWidth * rate);
				newHeight = maxRes;
			}
		}


		return Bitmap.createScaledBitmap(
				src, newWidth, newHeight, true);
	}

	private void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

		imgfile = new File(strFilePath);
		OutputStream out = null;

		try
		{
			imgfile.createNewFile();
			out = new FileOutputStream(imgfile);

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	**/
}
