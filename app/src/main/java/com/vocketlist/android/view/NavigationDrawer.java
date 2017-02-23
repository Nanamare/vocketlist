package com.vocketlist.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.vocketlist.android.R;
import com.vocketlist.android.activity.FacebookLoginActivity;
import com.vocketlist.android.roboguice.log.Ln;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SeungTaek.Lim on 2017. 2. 22..
 */

public class NavigationDrawer extends Fragment {
    @BindView(R.id.llLogin) protected LinearLayout mLoginView;
    @BindView(R.id.llLogout) protected LinearLayout mLogoutView;

    protected CallbackManager mCallbackManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.navigation_drawer_header, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        refreshUserInfo();
    }

    private void refreshUserInfo() {
        if (isLogined()) {
            switchLogin();
        } else {
            switchLogout();
        }
    }

    @OnClick(R.id.login_button)
    protected void onClickFacebookLoginBtn() {
        Ln.d("facebook login button click");

        Intent intent = new Intent(getContext(), FacebookLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.btnLogout)
    protected void onClickLogoutBtn() {
        LoginManager.getInstance().logOut();
        refreshUserInfo();
    }

    private boolean isLogined() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.isExpired() == false;
    }

    private void switchLogin() {
        mLoginView.setVisibility(View.VISIBLE);
        mLogoutView.setVisibility(View.GONE);
    }

    private void switchLogout() {
        mLoginView.setVisibility(View.GONE);
        mLogoutView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
