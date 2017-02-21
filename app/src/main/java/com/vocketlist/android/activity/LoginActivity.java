package com.vocketlist.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by SeungTaek.Lim on 2017. 2. 2..
 */

public class LoginActivity extends BaseActivity {
	@BindView(R.id.facebook_login_button)
	protected LoginButton facebookLoginButton;

	protected CallbackManager callbackManager;

	private ServiceManager serviceManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		serviceManager = new ServiceManager();

		this.callbackManager = CallbackManager.Factory.create();

		onClickFacebookLoginButton();


	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@OnClick
	protected void onClickFacebookLoginButton() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken != null) {
			// login이 되어있는 경우.

			return;
		}

		this.facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
		this.facebookLoginButton.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(final LoginResult loginResult) {
				Ln.d("LoginManager.onSuccess");
				AccessToken accessToken = loginResult.getAccessToken();
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String expiredAt = sdf.format(accessToken.getExpires().getTime());

				setFacebookData(loginResult);

			}

			@Override
			public void onCancel() {
				Ln.d("LoginManager.onCancel");
			}

			@Override
			public void onError(FacebookException exception) {
				Ln.d(exception, "LoginManager.onError");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.callbackManager.onActivityResult(requestCode, resultCode, data);
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
							Log.i("Link", link);
							if (Profile.getCurrentProfile() != null) {
								Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
							}

							Log.i("Login" + "Email", email);
							Log.i("Login" + "FirstName", firstName);
							Log.i("Login" + "LastName", lastName);
							Log.i("Login" + "Gender", gender);


							String userInfo = object.toString();
							String token = loginResult.getAccessToken().toString();
							String userId = loginResult.getAccessToken().getUserId();
							serviceManager.loginFb(userInfo, token, userId)
									.observeOn(AndroidSchedulers.mainThread())
									.subscribe(new Subscriber<Response<ResponseBody>>() {
										@Override
										public void onCompleted() {
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
												String response = responseBodyResponse.body().string();

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
