package com.vocketlist.android.activity;

import android.content.Intent;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vocketlist.android.AppApplication;
import com.vocketlist.android.R;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.roboguice.log.Ln;
import com.vocketlist.android.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
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

						AccessToken accessToken = loginResult.getAccessToken();
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
							String id = profile.getId();
							String link = profile.getLinkUri().toString();
							Ln.i("Link : " + link);
							if (Profile.getCurrentProfile() != null) {
								Ln.i("ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
							}

							Ln.i("Email : " + email);
							Ln.i("FirstName : " + firstName);
							Ln.i("LastName : " + lastName);
							Ln.i("Gender : " + gender);


							String userInfo = object.toString();
							String token = loginResult.getAccessToken().toString();
							String userId = loginResult.getAccessToken().getUserId();

							serviceManager.loginFb(userInfo, token, userId)
									.observeOn(AndroidSchedulers.mainThread())
									.doOnTerminate(new Action0() {
										@Override
										public void call() {
											finish();
										}
									})
									.subscribe(new Subscriber<Response<ResponseBody>>() {
										@Override
										public void onCompleted() {
											//완료 되었을시 페북 정보 클라이언트에 저장
											SharePrefUtil.putSharedPreference("email", email);
											SharePrefUtil.putSharedPreference("imgUrl", link);
											SharePrefUtil.putSharedPreference("fullName", lastName + firstName);

										}

										@Override
										public void onError(Throwable e) {

										}

										@Override
										public void onNext(Response<ResponseBody> responseBodyResponse) {
											try {
												String json = responseBodyResponse.body().string();
												JsonObject obj = (JsonObject) new JsonParser().parse(json);
												JsonObject jsonObject = (JsonObject) obj.get("result");
												//페북 로그인시 서버에서 내려주는 100줄짜리 토큰
												String token = jsonObject.getAsJsonPrimitive("token").getAsString();
												SharePrefUtil.putSharedPreference("token", token);

											} catch (IOException e) {
												e.printStackTrace();
											}

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
