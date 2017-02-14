package com.vocketlist.android.activity;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.vocketlist.android.R;
import com.vocketlist.android.roboguice.log.Ln;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SeungTaek.Lim on 2017. 2. 2..
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.facebook_login_button) protected LoginButton facebookLoginButton;

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
            public void onSuccess(LoginResult loginResult) {
                Ln.d("LoginManager.onSuccess");
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
