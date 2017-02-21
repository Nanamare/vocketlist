package com.vocketlist.android.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.vocketlist.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 커뮤니티 : 작성 + 수정
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class PostCUActivity extends DepthBaseActivity {

	private static final int REQUEST_WRITE_STORAGE = 112;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int RUQUEST_IMAGE_FROM_ALBUM = 5;

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.activity_post_create_update_picTv)
	TextView picTv;
	@BindView(R.id.activity_post_create_update_picIv)
	ImageView picIv;
	@BindView(R.id.activity_post_create_update_shareToFb_tv)
	TextView shareToFb_tv;

	private Bitmap bp;
	private File imgfile;
	private String mCurrentPhotoPath;


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

		// 헤더 CI 적용
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(
				getLayoutInflater().inflate(R.layout.appbar_sub_title, null),
				new ActionBar.LayoutParams(
						ActionBar.LayoutParams.WRAP_CONTENT,
						ActionBar.LayoutParams.WRAP_CONTENT,
						Gravity.CENTER
				)
		);

		checkThePemission();

		picTv.setOnClickListener(view -> takepicture());
		shareToFb_tv.setOnClickListener(view -> shareToFacebook());


	}

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

}
