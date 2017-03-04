package com.vocketlist.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;
import com.vocketlist.android.api.ServiceManager;
import com.vocketlist.android.api.user.LoginModel;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.preference.FacebookPreperence;
import com.vocketlist.android.roboguice.log.Ln;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by SeungTaek.Lim on 2017. 2. 2..
 */

public class FacebookLoginActivity extends BaseActivity {
	private CallbackManager mFacebookCallbackManager;
	private ServiceManager serviceManager;
	private AccessToken accessToken;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		serviceManager = new ServiceManager();


		initFacebook();
	}

	private void initFacebook() {
		mFacebookCallbackManager = CallbackManager.Factory.create();

		AppEventsLogger.activateApp(AppApplication.getInstance());
		mFacebookCallbackManager = CallbackManager.Factory.create();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		runFacebookLogin();
	}

	protected void runFacebookLogin() {
		LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						Ln.i("runFacebookLogin onSuccess()");

						accessToken = loginResult.getAccessToken();
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String expiredAt = sdf.format(accessToken.getExpires().getTime());

						setFacebookData(loginResult);
					}

					@Override
					public void onCancel() {
						Ln.d("runFacebookLogin onCancel()");
						finish();
					}

					@Override
					public void onError(FacebookException exception) {
						Ln.e("runFacebookLogin onError(). reason : " + exception.toString());
						finish();
					}
				});

		LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
	}


	public void setFacebookData(final LoginResult loginResult) {
		GraphRequest request = GraphRequest.newMeRequest(
				loginResult.getAccessToken(),
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {
						// Application code
						try {
							Log.i("Response", response.toString());

							String email = response.getJSONObject().getString("email");
							String firstName = response.getJSONObject().getString("first_name");
							String lastName = response.getJSONObject().getString("last_name");
							String gender = response.getJSONObject().getString("gender");


							Profile profile = Profile.getCurrentProfile();
//							String id = profile.getId();
							String link = profile.getLinkUri().toString();
							Ln.i("Link : " + link);
							String imgUrl = "";
							if (Profile.getCurrentProfile() != null) {
								Uri imgUri = Profile.getCurrentProfile().getProfilePictureUri(250, 250);
								Ln.i("ProfilePic" + imgUrl);
								imgUrl = imgUri.toString();
							}

							Ln.i("Email : " + email);
							Ln.i("FirstName : " + firstName);
							Ln.i("LastName : " + lastName);
							Ln.i("Gender : " + gender);


							String userInfo = object.toString();
							String token = accessToken.getToken();
							String userId = loginResult.getAccessToken().getUserId();

							FacebookPreperence.getInstance().setEmail(email);
							FacebookPreperence.getInstance().setUserImageUrl(imgUrl);
							FacebookPreperence.getInstance().setUserName(lastName + firstName);
							FacebookPreperence.getInstance().setUserInfo(object.toString());
							FacebookPreperence.getInstance().setUserId(userId);

							UserServiceManager.loginWithFacebook(userInfo, token, userId)
									.observeOn(AndroidSchedulers.mainThread())
									.doOnTerminate(new Action0() {
										@Override
										public void call() {
											setResult(RESULT_OK);
											finish();
										}
									})
									.subscribe(new Subscriber<Response<BaseResponse<LoginModel>>>() {
										@Override
										public void onCompleted() {
											//완료되었을시 fcm 토큰을 발행하고 서버에 등록시킨다.

										}

										@Override
										public void onError(Throwable e) {

										}

										@Override
										public void onNext(Response<BaseResponse<LoginModel>> baseResponseResponse) {
										}
									});


						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
		Bundle parameters = new Bundle();
		parameters.putString("fields", "first_name,last_name,verified,name,locale,gender,updated_time,link,id,timezone,email");
		request.setParameters(parameters);
		request.executeAsync();
	}

}
