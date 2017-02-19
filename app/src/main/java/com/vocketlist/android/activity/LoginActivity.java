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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.vocketlist.android.R;
import com.vocketlist.android.roboguice.log.Ln;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 2. 2..
 */

public class LoginActivity extends BaseActivity {
	@BindView(R.id.facebook_login_button)
	protected LoginButton facebookLoginButton;

	protected CallbackManager callbackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		this.callbackManager = CallbackManager.Factory.create();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@OnClick
	protected void onClickFacebookLoginButton() {
		AccessToken token = AccessToken.getCurrentAccessToken();

		if (token != null) {
			// login이 되어있는 경우.
			return;
		}

		this.facebookLoginButton.setReadPermissions("email");
		this.facebookLoginButton.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(final LoginResult loginResult) {
				Ln.d("LoginManager.onSuccess");
				AccessToken accessToken = loginResult.getAccessToken();
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String expiredAt = sdf.format(accessToken.getExpires().getTime());

				GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {
						try {
							//처리 로직 필요
							Log.d("facebook object",object.toString());
							String emial = object.getString("email");
							String name = object.getString("name");
							String gender = object.getString("gender");
							String id = object.getString("id");

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				Bundle parameter = new Bundle();
				parameter.putString("filed","id,name,email,gender");
				request.setParameters(parameter);
				request.executeAsync();

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
